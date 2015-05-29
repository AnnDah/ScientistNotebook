package controllers;

import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import exceptions.CreationException;
import exceptions.DeletionException;
import exceptions.GetException;
import exceptions.UpdateException;
import models.DatabaseConnector;
import models.Organization;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.*;

/**
 * Class to handle CRUD operations on organizations to database.
 *
 * @author Sebastian Nirfelt
 * @version 1.0 - 27/04/15
 */
public class OrganizationController {
    private DatabaseConnector db;
    private Mapper<Organization> mapper;

    public OrganizationController() {
        db = new DatabaseConnector();
        db.connectDefault();

        mapper = new MappingManager(db.getSession()).mapper(Organization.class);
    }

    /**
     * Creates a new organization and saves it in database
     * @param strOrganization Organization information to be saved
     * @return a JSONObject of the new Organization
     * @throws CreationException
     */
    public JSONObject create(String strOrganization) throws CreationException {
        // Create a random UUID
        UUID id = UUID.randomUUID();
        Organization org = null;

        try{
            // Parse the string of information into a JSONObject
            JSONObject jObj = (JSONObject) new JSONParser().parse(strOrganization);
            // Set values to the variables
            String name = (String) jObj.get("name");
            String description = (String) jObj.get("description");
            String policy = (String) jObj.get("policy");
            String license = (String) jObj.get("license");

            JSONArray departmentsArray = (JSONArray) jObj.get("departments");
            List<String> departments = new ArrayList<String>();
            for (Object aDepartmentsArray : departmentsArray) {
                departments.add(aDepartmentsArray.toString());
            }

            // Create the organization object
            org = new Organization(id, name, description, policy, license, departments);
            // Save the object in database
            mapper.save(org);

        } catch (ParseException e) {
            throw new CreationException("Invalid input data");
        } finally {
            db.close();
        }
        // Return a JSONObject of the organization
        return org.toJson();
    }

    /**
     * Removes a specific organization from database
     * @param id    id of the organizatio to delete
     * @throws DeletionException
     */
    public void delete(String id) throws DeletionException {
        try {
            mapper.delete(mapper.get(UUID.fromString(id)));

        } catch (IllegalArgumentException e) {
            throw new DeletionException("Organization wasn't found in database");
        } finally {
            db.close();
        }
    }

    /**
     * Gets a specific organization and returns it as a JSONObject
     * @param id    id of the organization to get
     * @return      a JSONObject of the organization
     * @throws GetException
     */
    public JSONObject get(String id)throws GetException {
        Organization org = getOrganization(id);
        db.close();
        return org.toJson();
    }

    /**
     * Gets a specific organization from database
     * @param id    id of the organization to get
     * @return      the organization object
     * @throws GetException
     */
    @SuppressWarnings("unchecked")
    private Organization getOrganization(String id) throws GetException {
        try {
            return mapper.get(UUID.fromString(id));
        } catch (IllegalArgumentException e) {
            throw new GetException("Organization wasn't found in database");
        }
    }

    /**
     * Updates a specific organization
     * @param id        id of the organization to update
     * @param update    the update information
     * @return          a JSONObject of the updated organization
     * @throws UpdateException
     */
    public JSONObject update(String id, String update) throws UpdateException {
        String name;
        String description;
        String policy;
        String license;
        List<String> departments;

        try {
            // Parse the information string into a JSONObject
            JSONObject jObj = (JSONObject) new JSONParser().parse(update);
            // Se values to the variables
            name = (String) jObj.get("name");
            description = (String) jObj.get("description");
            policy = (String) jObj.get("policy");
            license = (String) jObj.get("licence");

            JSONArray departmentsArray = (JSONArray) jObj.get("departments");
            departments = new ArrayList<String>();
            for (Object department : departmentsArray) {
                departments.add(department.toString());
            }

        }  catch (org.json.simple.parser.ParseException e) {
            throw new UpdateException("Invalid input data");
        } catch (IllegalArgumentException e) {
            throw new UpdateException("Invalid input data");
        }

        try {
            // Get the organization to update
            Organization org = getOrganization(id);

            // Set new vaules to organization fields
            org.setName(name);
            org.setDescription(description);
            org.setDepartments(departments);
            org.setLicense(license);
            org.setPolicy(policy);

            // Save the updated organization
            mapper.save(org);

            // Return a JSONObject of the updated organization
            return org.toJson();
        } catch (IllegalArgumentException e) {
            throw new UpdateException("User wasn't found in database");
        } catch (NullPointerException e) {
            throw new UpdateException("Invalid input data");
        } catch (GetException e) {
            throw new UpdateException("Invalid input data");
        }finally {
            db.close();
        }
    }

}
