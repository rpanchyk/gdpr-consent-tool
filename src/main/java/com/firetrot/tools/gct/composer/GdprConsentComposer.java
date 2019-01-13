package com.firetrot.tools.gct.composer;

import com.firetrot.tools.gct.exception.BadRequestException;
import com.firetrot.tools.gct.proto.request.ComposeRequest;
import com.iab.gdpr.consent.VendorConsent;
import com.iab.gdpr.consent.VendorConsentEncoder;
import com.iab.gdpr.consent.implementation.v1.VendorConsentBuilder;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.function.Consumer;

public class GdprConsentComposer {

    /**
     * Actually, there is no need for {@link Mono} wrapper here.
     * This is just for learning purpose.
     */
    public Mono<String> compose(ComposeRequest request) {
        final VendorConsentBuilder vendorConsentBuilder = new VendorConsentBuilder()
                .withConsentRecordCreatedOn(Instant.now())
                .withConsentRecordLastUpdatedOn(Instant.now());

        setIfNotNull(request.getCmpId(), vendorConsentBuilder::withCmpID);
        setIfNotNull(request.getCmpVersion(), vendorConsentBuilder::withCmpVersion);
        setIfNotNull(request.getConsentScreen(), vendorConsentBuilder::withConsentScreenID);
        setIfNotNull(request.getConsentLanguage(), vendorConsentBuilder::withConsentLanguage);
        setIfNotNull(request.getVendorListVersion(), vendorConsentBuilder::withVendorListVersion);
        setIfNotNull(request.getAllowedPurposeIds(), vendorConsentBuilder::withAllowedPurposeIds);
        setIfNotNull(request.getMaxVendorId(), vendorConsentBuilder::withMaxVendorId);
        setIfNotNull(request.getAllowedVendorIds(), vendorConsentBuilder::withBitField);

        final VendorConsent vendorConsent;
        try {
            vendorConsent = vendorConsentBuilder.build();
        } catch (Exception e) {
            throw new BadRequestException(e);
        }

        return Mono.just(VendorConsentEncoder.toBase64String(vendorConsent));
    }

    private static <T> void setIfNotNull(T object, Consumer<T> consumer) {
        if (object != null) {
            consumer.accept(object);
        }
    }
}
