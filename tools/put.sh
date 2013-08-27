



# resty localhost:9001 -H "Accept: application/json" -u user:pass

resty localhost:9000 -H "Accept: application/json" -H "Content-Type: application/json"

PUT /handhistory '{"id":"anid3","source":"made up 3","timestamp":"2013-07-14 19:43:37 +0100","raw":"somting3"}'

PUT /handhistory < sample.json