#!/usr/bin/env bash
set -m

/entrypoint.sh couchbase-server &

sleep 15

# Setup index and memory quota
curl -v -X POST http://127.0.0.1:8091/pools/default -d memoryQuota=512 -d indexMemoryQuota=512

# Setup services
curl -v http://127.0.0.1:8091/node/controller/setupServices -d services=kv%2Cn1ql%2Cindex

# Setup credentials
curl -v http://127.0.0.1:8091/settings/web -d port=8091 -d username=Administrator -d password=password


# Create buckets
curl -v -X POST -u Administrator:password -d name=note -d ramQuotaMB=256 -d authType=none  -d flushEnabled=1 http://127.0.0.1:8091/pools/default/buckets

#Create user to access bucket
curl -v -X PUT http://127.0.0.1:8091/settings/rbac/users/local/ramazan \
-u Administrator:password \
-d password=ramo123 \
-d roles=bucket_full_access[note]

# Create primary index for bucket
curl -v -X POST -u Administrator:password http://127.0.0.1:8091/settings/indexes -d 'indexerThreads=0' -d 'logLevel=info' -d 'maxRollbackPoints=5' -d 'memorySnapshotInterval=200' -d 'stableSnapshotInterval=5000' -d 'storageMode=forestdb'
sleep 10
curl -v -u Administrator:password http://127.0.0.1:8093/query/service -d statement=CREATE%20PRIMARY%20INDEX%20primary_index%20ON%20default:note%20USING%20GSI


echo "Type: $TYPE"


if [ "$TYPE" = "WORKER" ]; then
  sleep 15

  IP=`hostname -I`

  echo "Auto Rebalance: $AUTO_REBALANCE"
  if [ "$AUTO_REBALANCE" = "true" ]; then
    couchbase-cli rebalance --cluster=$COUCHBASE_MASTER:8091 --user=Administrator --password=password --server-add=$IP --server-add-username=Administrator --server-add-password=password
  else
    couchbase-cli server-add --cluster=$COUCHBASE_MASTER:8091 --user=Administrator --password=password --server-add=$IP --server-add-username=Administrator --server-add-password=password
  fi;
fi;

fg 1