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

### Topology transfer function (links)

Add bidirectional link between two faces "f1" and "f2" (assuming all faces are labeled uniquely)

```
-link   <f1>    <f2>
```

