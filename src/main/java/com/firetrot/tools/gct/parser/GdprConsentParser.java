package com.firetrot.tools.gct.parser;

import com.firetrot.tools.gct.exception.BadRequestException;
import com.firetrot.tools.gct.exception.InternalServerException;
import com.firetrot.tools.gct.parser.model.GdprConsent;
import com.iab.gdpr.consent.VendorConsent;
import com.iab.gdpr.consent.VendorConsentDecoder;
import reactor.core.publisher.Mono;

public class GdprConsentParser {

    public Mono<GdprConsent> parse(String consent) {
        try {
            return Mono.just(toGdprConsent(VendorConsentDecoder.fromBase64String(consent)));
        } catch (IllegalArgumentException e) {
            return Mono.error(new BadRequestException(e));
        } catch (Throwable e) {
            return Mono.error(new InternalServerException(e));
        }
    }

    private GdprConsent toGdprConsent(VendorConsent vendorConsent) {
        return GdprConsent.builder()
                .version(vendorConsent.getVersion())
                .consentRecordCreated(vendorConsent.getConsentRecordCreated())
                .consentRecordLastUpdated(vendorConsent.getConsentRecordLastUpdated())
                .cmpId(vendorConsent.getCmpId())
                .cmpVersion(vendorConsent.getCmpVersion())
                .consentScreen(vendorConsent.getConsentScreen())
                .consentLanguage(vendorConsent.getConsentLanguage())
                .vendorListVersion(vendorConsent.getVendorListVersion())
                .allowedPurposeIds(vendorConsent.getAllowedPurposeIds())
                .allowedPurposes(vendorConsent.getAllowedPurposes())
                .allowedPurposesBits(vendorConsent.getAllowedPurposesBits())
                .maxVendorId(vendorConsent.getMaxVendorId())
                .build();
    }
}
