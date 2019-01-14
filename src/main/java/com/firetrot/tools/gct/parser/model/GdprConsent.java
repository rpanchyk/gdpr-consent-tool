package com.firetrot.tools.gct.parser.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iab.gdpr.Purpose;
import lombok.Builder;
import lombok.Value;

import java.time.ZonedDateTime;
import java.util.Set;

@Builder
@Value
public class GdprConsent {

    int version;

    ZonedDateTime consentRecordCreated;

    ZonedDateTime consentRecordLastUpdated;

    int cmpId;

    int cmpVersion;

    int consentScreen;

    String consentLanguage;

    int vendorListVersion;

    int maxVendorId;

    @JsonIgnore
    Set<Purpose> allowedPurposes;

    @JsonIgnore
    int allowedPurposesBits;

    Set<Integer> allowedPurposeIds;

    Set<Integer> allowedVendorIds;
}
