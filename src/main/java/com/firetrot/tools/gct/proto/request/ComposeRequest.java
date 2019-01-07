package com.firetrot.tools.gct.proto.request;

import lombok.Builder;
import lombok.Value;

import java.util.Set;

@Builder
@Value
public class ComposeRequest {

    Integer cmpId;

    Integer cmpVersion;

    Integer consentScreen;

    String consentLanguage;

    Integer vendorListVersion;

    Set<Integer> allowedPurposeIds;

    Integer maxVendorId;

    Set<Integer> allowedVendorIds;
}
