package com.firetrot.tools.gct.composer;

import com.firetrot.tools.gct.proto.request.ComposeRequest;
import com.iab.gdpr.consent.VendorConsentEncoder;
import com.iab.gdpr.consent.implementation.v1.VendorConsentBuilder;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.function.Consumer;

public class GdprConsentComposer {

    public Mono<String> compose(ComposeRequest composeRequest) {
        final VendorConsentBuilder vendorConsentBuilder = new VendorConsentBuilder()
                .withConsentRecordCreatedOn(Instant.now())
                .withConsentRecordLastUpdatedOn(Instant.now());

        setIfNotNull(composeRequest.getCmpId(), vendorConsentBuilder::withCmpID);
        setIfNotNull(composeRequest.getCmpVersion(), vendorConsentBuilder::withCmpVersion);
        setIfNotNull(composeRequest.getConsentScreen(), vendorConsentBuilder::withConsentScreenID);
        setIfNotNull(composeRequest.getConsentLanguage(), vendorConsentBuilder::withConsentLanguage);
        setIfNotNull(composeRequest.getVendorListVersion(), vendorConsentBuilder::withVendorListVersion);
        setIfNotNull(composeRequest.getAllowedPurposeIds(), vendorConsentBuilder::withAllowedPurposeIds);
        setIfNotNull(composeRequest.getMaxVendorId(), vendorConsentBuilder::withMaxVendorId);
        setIfNotNull(composeRequest.getAllowedVendorIds(), vendorConsentBuilder::withBitField);

        return Mono.just(VendorConsentEncoder.toBase64String(vendorConsentBuilder.build()));
    }

    private <T> void setIfNotNull(T object, Consumer<T> consumer) {
        if (object != null) {
            consumer.accept(object);
        }
    }
}
