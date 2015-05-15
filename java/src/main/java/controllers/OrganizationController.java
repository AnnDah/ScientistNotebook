package controllers;

import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import exceptions.CreationException;
import exceptions.DeletionException;
import exceptions.GetException;
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

    public UUID createOrganization(String strOrganization) throws CreationException{
        DatabaseConnector db = new DatabaseConnector();
        db.connectDefault();

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

            Mapper<Organization> mapper = new MappingManager(db.getSession()).mapper(Organization.class);
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
        DatabaseConnector db = new DatabaseConnector();
        db.connectDefault();
        try {
            Mapper<Organization> mapper = new MappingManager(db.getSession()).mapper(Organization.class);
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
    public JSONObject createOrgJson(UUID id, String name, String description, String policy, String license,
                                    List<String> departments){


        JSONObject orgJson = new JSONObject();
        orgJson.put("id", id);
        orgJson.put("name", name);
        orgJson.put("description", description);
        orgJson.put("policy", policy);
        orgJson.put("license", license);
        orgJson.put("departments", departments);

        /**
         Only needs to be implemented when we need to get multiple users
         JSONArray ja = new JSONArray();
         ja.add(jObj);
         JSONObject mainObj = new JSONObject();
         mainObj.put("users", ja);
         System.out.println(mainObj);
         */

        return orgJson;
    }

    public JSONObject getOrganization(String orgId) throws GetException{

        DatabaseConnector db = new DatabaseConnector();
        db.connectDefault();

        Organization whose;
        Mapper<Organization> mapper = new MappingManager(db.getSession()).mapper(Organization.class);
        try {
            UUID orgUuid = UUID.fromString(orgId);
            whose = mapper.get(orgUuid);
        } catch (IllegalArgumentException e){
            throw new GetException("Organization wasn't found in database");
        } finally {
            db.close();
        }

        return createOrgJson(
                whose.getId(),
                whose.getName(),
                whose.getDescription(),
                whose.getPolicy(),
                whose.getLicense(),
                whose.getDepartments());
    }
}
