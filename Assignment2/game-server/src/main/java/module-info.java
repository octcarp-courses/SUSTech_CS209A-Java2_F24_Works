module linkgame.server {
    requires transitive linkgame.common;
    requires org.apache.commons.csv;
    exports io.github.octcarp.sustech.cs209a.linkgame.server;
    exports io.github.octcarp.sustech.cs209a.linkgame.server.net;
}