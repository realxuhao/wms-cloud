#!/bin/bash

envs=$(printenv)

rm .env.production

array=(${envs//' '/ })

for element in "${array[@]}"
do
  if  [[ $element == VUE_APP_* ]] || [[ $element == NODE_ENV* ]];
  then
    echo "$element"
    echo "$element" >> .env.production
  fi
done

echo "final env production file content as:"
cat .env.production

npm run build:production

echo 'build succeed'

serve -s dist -l 8000

