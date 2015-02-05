package com.matelau.junior.centsproject.Models.Design.GlassdoorAPIModels;

import com.google.gson.annotations.Expose;

/**
 * Created by matelau on 11/29/14.
 */
public class Employer {

    @Expose
    public Integer id;
    @Expose
    public String name;
    @Expose
    public String website;
    @Expose
    public Boolean isEEP;
    @Expose
    public Boolean exactMatch;
    @Expose
    public String industry;
    @Expose
    public Integer numberOfRatings;
    @Expose
    public String squareLogo;
    @Expose
    public float overallRating;
    @Expose
    public String ratingDescription;
    @Expose
    public String cultureAndValuesRating;
    @Expose
    public String seniorLeadershipRating;
    @Expose
    public String compensationAndBenefitsRating;
    @Expose
    public String careerOpportunitiesRating;
    @Expose
    public String workLifeBalanceRating;
    @Expose
    public FeaturedReview featuredReview;
    @Expose
    public Ceo ceo;
}
