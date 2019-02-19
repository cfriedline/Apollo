package org.bbop.apollo.gwt.client.dto;

import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/**
 * Created by ndunn on 12/18/14.
 */
public class TrackInfo implements Comparable<TrackInfo> {


    public static final String TRACK_UNCATEGORIZED = "Uncategorized";

    private String name;
    private String label;
    private String type;
    private Boolean visible;


    private Boolean isPublic ;
    private OrganismInfo organismInfo ;

    private String urlTemplate ;
    private String category;

    private JSONObject payload ;

    public TrackInfo(){}

    @Override
    public int compareTo(TrackInfo o) {
        return name.compareTo(o.name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUrlTemplate() {
        return urlTemplate;
    }

    public void setUrlTemplate(String urlTemplate) {
        this.urlTemplate = urlTemplate;
    }

    public JSONObject getPayload() {
        return payload;
    }

    public void setPayload(JSONObject payload) {
        this.payload = payload;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public OrganismInfo getOrganismInfo() {
        return organismInfo;
    }

    public void setOrganismInfo(OrganismInfo organismInfo) {
        this.organismInfo = organismInfo;
    }

    public String getStandardCategory(){
        if(category==null || category.trim().length()==0){
            return TRACK_UNCATEGORIZED ;
        }
        else{
            String categoryString = "";
            for(String c : category.split("\\/")){
                categoryString+=c +"/";
            }
            return  categoryString.substring(0,categoryString.length()-1);
        }
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrackInfo)) return false;

        TrackInfo trackInfo = (TrackInfo) o;

        if (!getName().equals(trackInfo.getName())) return false;
        if (getLabel() != null ? !getLabel().equals(trackInfo.getLabel()) : trackInfo.getLabel() != null) return false;
        if (!getType().equals(trackInfo.getType())) return false;
        if (!getCategory().equals(trackInfo.getCategory())) return false;
        return getUrlTemplate() != null ? getUrlTemplate().equals(trackInfo.getUrlTemplate()) : trackInfo.getUrlTemplate() == null;

    }

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + (getLabel() != null ? getLabel().hashCode() : 0);
        result = 31 * result + getType().hashCode();
        result = 31 * result + (getUrlTemplate() != null ? getUrlTemplate().hashCode() : 0);
        result = 31 * result + (getCategory() != null ? getCategory().hashCode() : 0);
        return result;
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", new JSONString(name));
        jsonObject.put("label", new JSONString(label));
        jsonObject.put("type", new JSONString(type));
        jsonObject.put("visible", JSONBoolean.getInstance(visible));
        jsonObject.put("public", JSONBoolean.getInstance(isPublic));
        jsonObject.put("urlTemplate", new JSONString(urlTemplate));
        jsonObject.put("category", new JSONString(category));
        jsonObject.put("payload", payload);
        jsonObject.put("organism", organismInfo.toJSON());
        return jsonObject;
    }

}
