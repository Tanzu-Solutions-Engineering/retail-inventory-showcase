
Starting GemFire

Open Gfsh

```shell
export GEMFIRE_HOME=/Users/devtools/repositories/IMDG/gemfire/vmware-gemfire-9.15.3
cd $GEMFIRE_HOME/bin
./gfsh
```

Start Locator

```shell
start locator --name=locator1 --bind-address=127.0.0.1 --hostname-for-clients=127.0.0.1  --jmx-manager-hostname-for-clients=127.0.0.1 --http-service-bind-address=127.0.0.1 
```

Start Server

```shell
start server --name=server1 --server-port=40404  --locators=127.0.0.1[10334]  --bind-address=127.0.0.1 --hostname-for-clients=127.0.0.1  --jmx-manager-hostname-for-clients=127.0.0.1 --http-service-bind-address=127.0.0.1
```

Create Regions

```shell
run --file=/Users/Projects/VMware/Tanzu/Use-Cases/Vertical-Industries/VMware-Retail/dev/vmware-retail-inventory-showcase/deployments/repositories/gemfire/create_regions.gfsh
```