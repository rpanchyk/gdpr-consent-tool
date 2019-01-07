#!/bin/sh

curl -X POST \
http://localhost:8080/api \
  -H 'Content-Type: application/json' \
  -d '{
    "cmp_id": 1,
    "cmp_version": 1,
    "consent_screen": 1,
    "consent_language": "EN",
    "vendor_list_version": 113,
    "allowed_purpose_ids": [1, 3],
    "max_vendor_id": 544,
    "allowed_vendor_ids": [32, 52]
}
'
