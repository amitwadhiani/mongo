#!/bin/bash
chart="provider-api"

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

echo -e "\nUpgrading git with these changes...\n"
git diff

echo -e "\nUpgrading charts: helm upgrade $chart $DIR/$chart/ -f $DIR/$chart/values.yaml ...\n"
#helm install --name $chart $DIR/$chart/ -f $DIR/$chart/values.yaml
helm upgrade $chart $DIR/$chart/ -f $DIR/$chart/values.yaml
