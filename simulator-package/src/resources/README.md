# Installation
## Prerequisites

### Java 7

This service makes use of the Java 7 runtime. The runtime can be downloaded
from the Oracle Homepage. I recommend downloading the complete JDK from

[http://www.oracle.com/technetwork/java/javase/downloads/index.html](http://www.oracle.com/technetwork/java/javase/downloads/index.html)

### Groovy

In addition to the JDK the Groovy scripting language needs to be available. At
lease version 2.1 is needed. It can be downloaded from

[http://groovy.codehaus.org/Download](http://groovy.codehaus.org/Download)

## Start Service

To start the service run the 'start.bat' file located the simulator root
directory.

## Testing

The default installation comes with a few initial scripts that can be used
to check whether the simulator is working as expected.

Once the service has started open browser capable using websockets and
browse to the location:

[http://localhost:5353/control](http://localhost:5353/control)

This opens a simple websocket test page automatically connecting to the
simulator. When sending the text "Hello, World!" the client will receive
an all upper case answer back. This indicates the simulator is working
as expected.

For a more detailed explanation how to write custom simulator event
please visit the projects [wiki page][wiki].

[wiki]: https://github.com/Domo42/external-party-simulator/wiki
