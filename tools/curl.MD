



# resty localhost:9001 -H "Accept: application/json" -u user:pass

resty localhost:9000 -H "Accept: application/json" -H "Content-Type: application/json"


curl localhost:8080/handhistory/2 -H "Accept: application/json" -H "Content-Type: application/json" -X PUT -d '{"handId":"2", "handHistory":"made up 3"}'


PUT /handhistory < sample.json