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
 * Created by niffe on 2015-04-27.
 *
 */
public class OrganizationController {
    private DatabaseConnector db;
    private Mapper<Organization> mapper;

    public OrganizationController(){
        db = new DatabaseConnector();
        db.connectDefault();

        mapper = new MappingManager(db.getSession()).mapper(Organization.class);
    }

    public UUID createOrganization(String strOrganization) throws CreationException{
        UUID id = UUID.randomUUID();

        try{
            JSONObject jObj = (JSONObject) new JSONParser().parse(strOrganization);
            String name = (String) jObj.get("name");
            String description = (String) jObj.get("description");
            String policy = (String) jObj.get("policy");
            String license = (String) jObj.get("license");

            JSONArray departmentsArray = (JSONArray) jObj.get("departments");
            List<String> departments = new ArrayList<String>();
            for (Object aDepartmentsArray : departmentsArray) {
                departments.add(aDepartmentsArray.toString());
            }

            Organization org = new Organization(id, name, description, policy, license, departments);
            mapper.save(org);

        } catch (ParseException e){
            throw new CreationException("Invalid input data");
        } finally {
            db.close();
        }
        return id;
    }

    public void deleteOrganization(String orgId) throws DeletionException{
        try {
            UUID id = UUID.fromString(orgId);
            Organization whose = mapper.get(id);
            mapper.delete(whose);

        } catch (IllegalArgumentException e){
            throw new DeletionException("Organization wasn't found in database");
        } finally {
            db.close();
        }
    }

    @SuppressWarnings("unchecked")
    public JSONObject createOrgJson(Organization whose){
        UUID id = whose.getId();
        String name = whose.getName();
        String description = whose.getDescription();
        String policy = whose.getPolicy();
        String license = whose.getLicense();
        List<String> departments = whose.getDepartments();

        JSONObject orgJson = new JSONObject();
        orgJson.put("id", id);
        orgJson.put("name", name);
        orgJson.put("description", description);
        orgJson.put("policy", policy);
        orgJson.put("license", license);
        orgJson.put("departments", departments);

        JSONArray ja = new JSONArray();
        ja.add(orgJson);

        JSONObject mainObj = new JSONObject();
        mainObj.put("organizations", ja);

        return mainObj;
    }

    public JSONObject getOrganizationJson(String id)throws GetException{
        Organization org = getOrganization(id);
        db.close();
        return createOrgJson(org);
    }
    @SuppressWarnings("unchecked")
    public Organization getOrganization(String orgId) throws GetException {
        try {
            UUID orgUuid = UUID.fromString(orgId);
            return mapper.get(orgUuid);
        } catch (IllegalArgumentException e) {
            throw new GetException("Organization wasn't found in database");
        }
    }

    public JSONObject updateOrganization(String id, String update) throws UpdateException{
        String name;
        String description;
        String policy;
        String license;
        List<String> departments;

        try {
            JSONObject jObj = (JSONObject) new JSONParser().parse(update);

            name = (String) jObj.get("name");
            description = (String) jObj.get("description");
            policy = (String) jObj.get("policy");
            license = (String) jObj.get("licence");

            JSONArray departmentsArray = (JSONArray) jObj.get("departments");
            departments = new ArrayList<String>();
            for (Object department : departmentsArray) {
                departments.add(department.toString());
            }

        }  catch (org.json.simple.parser.ParseException e){
            throw new UpdateException("Invalid input data");
        } catch (IllegalArgumentException e){
            throw new UpdateException("Invalid input data");
        }

        try {
            Organization org = getOrganization(id);

            org.setName(name);
            org.setDescription(description);
            org.setDepartments(departments);
            org.setLicense(license);
            org.setPolicy(policy);

            mapper.save(org);


            return createOrgJson(org);
        } catch (IllegalArgumentException e){
            throw new UpdateException("User wasn't found in database");
        } catch (NullPointerException e){
            throw new UpdateException("Invalid input data");
        } catch (GetException e){
            throw new UpdateException("Invalid input data");
        }finally {
            db.close();
        }
    }

    private UUID stringToUUID(String id){
        return UUID.fromString(id);
    }

}
