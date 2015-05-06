package com.matelau.junior.centsproject.Models.UserModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matelau on 4/7/15.
 */
public class UserResponse {
    @Expose
    private Long id;
    @Expose
    private String email;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("email_type")
    @Expose
    private String emailType;
    @SerializedName("primary_color")
    @Expose
    private String primaryColor;
    @SerializedName("secondary_color")
    @Expose
    private String secondaryColor;
    @SerializedName("prefers_autocomplete")
    @Expose
    private Boolean prefersAutocomplete;
    @SerializedName("completed_sections")
    @Expose
    private List<String> completedSections = new ArrayList<String>();
    @SerializedName("spending_breakdown_data")
    @Expose
    private List<SpendingBreakdownDatum> spendingBreakdownData = new ArrayList<SpendingBreakdownDatum>();
    @SerializedName("career_ratings")
    @Expose
    private List<CareerRating> careerRatings = new ArrayList<CareerRating>();
    @SerializedName("degree_ratings")
    @Expose
    private List<DegreeRating> degreeRatings = new ArrayList<DegreeRating>();
    @SerializedName("school_ratings")
    @Expose
    private List<SchoolRating> schoolRatings = new ArrayList<SchoolRating>();
    @Expose
    private List<String> queries = new ArrayList<String>();

    /**
     *
     * @return
     * The id
     */
    public Long getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The email
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     * The email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return
     * The firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     *
     * @param firstName
     * The first_name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     *
     * @return
     * The lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     *
     * @param lastName
     * The last_name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     *
     * @return
     * The emailType
     */
    public String getEmailType() {
        return emailType;
    }

    /**
     *
     * @param emailType
     * The email_type
     */
    public void setEmailType(String emailType) {
        this.emailType = emailType;
    }

    /**
     *
     * @return
     * The primaryColor
     */
    public String getPrimaryColor() {
        return primaryColor;
    }

    /**
     *
     * @param primaryColor
     * The primary_color
     */
    public void setPrimaryColor(String primaryColor) {
        this.primaryColor = primaryColor;
    }

    /**
     *
     * @return
     * The secondaryColor
     */
    public String getSecondaryColor() {
        return secondaryColor;
    }

    /**
     *
     * @param secondaryColor
     * The secondary_color
     */
    public void setSecondaryColor(String secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    /**
     *
     * @return
     * The prefersAutocomplete
     */
    public Boolean getPrefersAutocomplete() {
        return prefersAutocomplete;
    }

    /**
     *
     * @param prefersAutocomplete
     * The prefers_autocomplete
     */
    public void setPrefersAutocomplete(Boolean prefersAutocomplete) {
        this.prefersAutocomplete = prefersAutocomplete;
    }

    /**
     *
     * @return
     * The completedSections
     */
    public List<String> getCompletedSections() {
        return completedSections;
    }

    /**
     *
     * @param completedSections
     * The completed_sections
     */
    public void setCompletedSections(List<String> completedSections) {
        this.completedSections = completedSections;
    }

    /**
     *
     * @return
     * The spendingBreakdownData
     */
    public List<SpendingBreakdownDatum> getSpendingBreakdownData() {
        return spendingBreakdownData;
    }

    /**
     *
     * @param spendingBreakdownData
     * The spending_breakdown_data
     */
    public void setSpendingBreakdownData(List<SpendingBreakdownDatum> spendingBreakdownData) {
        this.spendingBreakdownData = spendingBreakdownData;
    }

    /**
     *
     * @return
     * The careerRatings
     */
    public List<CareerRating> getCareerRatings() {
        return careerRatings;
    }

    /**
     *
     * @param careerRatings
     * The career_ratings
     */
    public void setCareerRatings(List<CareerRating> careerRatings) {
        this.careerRatings = careerRatings;
    }

    /**
     *
     * @return
     * The degreeRatings
     */
    public List<DegreeRating> getDegreeRatings() {
        return degreeRatings;
    }

    /**
     *
     * @param degreeRatings
     * The degree_ratings
     */
    public void setDegreeRatings(List<DegreeRating> degreeRatings) {
        this.degreeRatings = degreeRatings;
    }

    /**
     *
     * @return
     * The schoolRatings
     */
    public List<SchoolRating> getSchoolRatings() {
        return schoolRatings;
    }

    /**
     *
     * @param schoolRatings
     * The school_ratings
     */
    public void setSchoolRatings(List<SchoolRating> schoolRatings) {
        this.schoolRatings = schoolRatings;
    }

    /**
     *
     * @return
     * The queries
     */
    public List<String> getQueries() {
        return queries;
    }

    /**
     *
     * @param queries
     * The queries
     */
    public void setQueries(List<String> queries) {
        this.queries = queries;
    }
}
