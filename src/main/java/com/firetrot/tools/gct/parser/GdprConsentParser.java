package com.firetrot.tools.gct.parser;

import com.firetrot.tools.gct.exception.BadRequestException;
import com.firetrot.tools.gct.exception.InternalServerException;
import com.firetrot.tools.gct.parser.model.GdprConsent;
import com.iab.gdpr.consent.VendorConsent;
import com.iab.gdpr.consent.VendorConsentDecoder;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.TreeSet;

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

    private static GdprConsent toGdprConsent(VendorConsent vendorConsent) {
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
                .allowedVendorIds(allowedVendorIds(vendorConsent))
                .build();
    }

    /**
     * Returns all possible vendor IDs for given {@link VendorConsent}.
     */
    private static Set<Integer> allowedVendorIds(VendorConsent vendorConsent) {
        final Set<Integer> allowedVendorIds = new TreeSet<>();
        for (int i = 0; i < vendorConsent.getMaxVendorId(); i++) {
            if (vendorConsent.isVendorAllowed(i)) {
                allowedVendorIds.add(i);
            }
        }
        return allowedVendorIds;
    }
}
