package org.jetlinks.core.defaults;

import lombok.Getter;
import org.jetlinks.core.ProtocolSupport;
import org.jetlinks.core.ProtocolSupports;
import org.jetlinks.core.config.ConfigStorage;
import org.jetlinks.core.config.ConfigStorageManager;
import org.jetlinks.core.config.StorageConfigurable;
import org.jetlinks.core.device.DeviceConfigKey;
import org.jetlinks.core.device.DeviceOperator;
import org.jetlinks.core.device.DeviceProductOperator;
import org.jetlinks.core.metadata.DeviceMetadata;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class DefaultDeviceProductOperator implements DeviceProductOperator, StorageConfigurable {

    @Getter
    private final String id;

    private final ProtocolSupports protocolSupports;

    private AtomicReference<DeviceMetadata> metadataCache = new AtomicReference<>();

    private Mono<ConfigStorage> storageMono;

    private Supplier<Flux<DeviceOperator>> devicesSupplier;

    @Deprecated
    public DefaultDeviceProductOperator(String id,
                                        ProtocolSupports supports,
                                        ConfigStorageManager manager) {
        this(id, supports, manager, Flux::empty);
    }

    public DefaultDeviceProductOperator(String id,
                                        ProtocolSupports supports,
                                        ConfigStorageManager manager,
                                        Supplier<Flux<DeviceOperator>> supplier) {
        this.id = id;
        this.protocolSupports = supports;
        storageMono = manager.getStorage("device-product:".concat(id));
        this.devicesSupplier = supplier;
    }

    @Override
    public Mono<DeviceMetadata> getMetadata() {
        return Mono.justOrEmpty(metadataCache.get())
                .switchIfEmpty(getProtocol()
                        .flatMap(protocol -> getConfig(DeviceConfigKey.metadata)
                                .flatMap(protocol.getMetadataCodec()::decode)
                                .doOnNext(metadataCache::set)));
    }

    @Override
    public Mono<Boolean> updateMetadata(String metadata) {
        return setConfig(DeviceConfigKey.metadata.value(metadata))
                .doOnSuccess((v) -> metadataCache.set(null));
    }


    @Override
    public Mono<ProtocolSupport> getProtocol() {
        return getConfig(DeviceConfigKey.protocol)
                .flatMap(protocolSupports::getProtocol);
    }

    @Override
    public Mono<ConfigStorage> getReactiveStorage() {
        return storageMono;
    }

    @Override
    public Flux<DeviceOperator> getDevices() {
        return devicesSupplier == null ? Flux.empty() : devicesSupplier.get();
    }
}
