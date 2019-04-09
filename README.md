# Name Space Analysis

A tool to verify Named Data Network data planes

## Network snapshot Input files format

Each line describes certain aspects of the snapshot and its data plane state. All elements within a line are separated by tabs. 
Look at the samples in the ```/examples``` folder for more details.

### Nodes and faces

Add node ID's and its face IDs

```
-node   <n>   <n1>  <n2>  <n3>
```

### Network transfer function (Rules)

Add rule for node "n" that for incoming header from "f_in" matching pattern "m", header should leave from "f_out" (assuming no change in header)

```
-rule   <n> <m> <f_in>  <f_out>
```

### Topology transfer function (Links)

Add bidirectional link between two faces "f1" and "f2" (assuming all faces are labeled uniquely)

```
-link   <f1>    <f2>
```

### Name Spaces (Content provider prefixes)

Add content provider "n"'s hosted prefix "p"

```
-provider   <n> <p>
```

### Private name spaces (Prohibited names)
Add constraint on name "p" that should not be reached at node "n" from other zones
```
-prohibited <n> <p>
```

### Injections
Inject header space "h" at face "f"
```
-inject "h" "f"
```

Inject header space "h" at all faces
```
-injectAll  "h"
```

## Face selection
You can restrict the "injectAll" mode to selected faces only, listing them in a file (look at example ```/examples/faces.txt```). 

## Zone selection
You can group nodes into zones, for name leakage checks, in a file (look at example ```/examples/zones.txt```). 