package controllers;

import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import models.DatabaseConnector;
import models.Organization;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.util.*;

/**
 * Created by niffe on 2015-04-27.
 *
 */
public class OrganizationController {

    public UUID createOrganization(String strOrganization){
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

        } catch (Exception e){
            System.out.println(e);
        }
        db.close();
        return id;
    }

    public int deleteOrganization(String orgId){
        if (orgId == null){
            System.out.println("No request parameter was provided");
            return 400;
        }

        UUID id = UUID.fromString(orgId);

        DatabaseConnector db = new DatabaseConnector();
        db.connectDefault();
        try {
            Mapper<Organization> mapper = new MappingManager(db.getSession()).mapper(Organization.class);
            Organization whose = mapper.get(id);
            if (whose == null){
                System.out.println("Organization wasn't found in database");
                db.close();
                return 404;
            }
            mapper.delete(whose);

        } catch (Exception e){
            System.out.println(e);
        }
        db.close();
        return 200;
    }

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

    public JSONObject getOrganization(String orgId){
        if (orgId == null){
            System.out.println("No request parameter was provided");
            return null;
        }

        UUID orgUuid = UUID.fromString(orgId);

        DatabaseConnector db = new DatabaseConnector();
        db.connectDefault();
        Organization whose;

        Mapper<Organization> mapper = new MappingManager(db.getSession()).mapper(Organization.class);
        whose = mapper.get(orgUuid);
        if (whose == null) {
            System.out.println("Organization wasn't found in database");
            db.close();
            return null;
        }

        UUID id =whose.getId();
        String name = whose.getName();
        String description = whose.getDescription();
        String policy = whose.getPolicy();
        String license = whose.getLicense();
        List<String> departments = whose.getDepartments();

        JSONObject user = createOrgJson(id, name, description, policy, license, departments);
        db.close();

        return user;
    }
}
