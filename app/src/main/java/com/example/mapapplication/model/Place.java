package com.example.mapapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * model data stored in predictions type field.
 */
public class Place implements  Parcelable{
    @SerializedName("description")
    private String description;

    @SerializedName("id")
    private String id;

    @SerializedName("matched_substrings")
    private List<MatchedSubstring> matchedSubstrings;

    @SerializedName("place_id")
    private String placeId;

    @SerializedName("reference")
    private String reference;

    @SerializedName("terms")
    private List<Term> terms;

    protected Place(Parcel in) {
        description = in.readString();
        id = in.readString();
        placeId = in.readString();
        reference = in.readString();
        types = in.createStringArrayList();
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<MatchedSubstring> getMatchedSubstrings() {
        return matchedSubstrings;
    }

    public void setMatchedSubstrings(List<MatchedSubstring> matchedSubstrings) {
        this.matchedSubstrings = matchedSubstrings;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public List<Term> getTerms() {
        return terms;
    }

    public void setTerms(List<Term> terms) {
        this.terms = terms;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    @SerializedName("types")
    private List<String> types;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeString(id);
        dest.writeString(placeId);
        dest.writeString(reference);
        dest.writeStringList(types);
    }
}
