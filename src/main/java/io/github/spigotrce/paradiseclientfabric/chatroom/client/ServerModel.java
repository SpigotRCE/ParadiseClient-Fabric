package io.github.spigotrce.paradiseclientfabric.chatroom.client;

import io.netty.handler.ssl.util.SelfSignedCertificate;

public record ServerModel(int port, boolean useHAProxy, SelfSignedCertificate ssc) {
}
