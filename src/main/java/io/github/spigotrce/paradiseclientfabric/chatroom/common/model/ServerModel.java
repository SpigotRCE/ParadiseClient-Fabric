package io.github.spigotrce.paradiseclientfabric.chatroom.common.model;

import io.netty.handler.ssl.util.SelfSignedCertificate;

public record ServerModel(int port, boolean useHAProxy, SelfSignedCertificate ssc) {
}
