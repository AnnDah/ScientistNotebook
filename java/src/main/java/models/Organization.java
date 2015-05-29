package models;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Objects;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by annikamagnusson on 08/05/15.
 *
 */
@Table(keyspace = "scinote", name = "organizations")
public class Organization {
    @PartitionKey
    private UUID id;
    private String name;
    private String description;
    private String policy;
    private String license;
    private List<String> departments = new ArrayList<String>();

    public Organization() {

    }

    public Organization(UUID id, String name, String description, String policy, String license, List<String> departments) {
        this.setId(id);
        this.setName(name);
        this.setDescription(description);
        this.setPolicy(policy);
        this.setLicense(license);
        this.setDepartments(departments);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public List<String> getDepartments() {
        return departments;
    }

    public void setDepartments(List<String> departments) {
        this.departments = departments;
    }

    @Override
    public boolean equals(Object other) {
        if(other instanceof Organization) {
            Organization that = (Organization) other;
            return Objects.equals(this.id, that.id) &&
                    Objects.equals(this.name, that.name) &&
                    Objects.equals(this.description, that.description) &&
                    Objects.equals(this.policy, that.policy) &&
                    Objects.equals(this.license, that.license) &&
                    Objects.equals(this.departments, that.departments);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, policy, license, departments);
    }

    @SuppressWarnings("unchecked")
    public JSONObject toJson() {
        JSONObject orgJson = new JSONObject();
        orgJson.put("id", this.getId().toString());
        orgJson.put("name", this.getName());
        orgJson.put("description", this.getDescription());
        orgJson.put("policy", this.getPolicy());
        orgJson.put("license", this.getLicense());
        orgJson.put("departments", this.getDepartments());

        JSONArray ja = new JSONArray();
        ja.add(orgJson);

        JSONObject mainObj = new JSONObject();
        mainObj.put("organizations", ja);

        return mainObj;
    }
}
