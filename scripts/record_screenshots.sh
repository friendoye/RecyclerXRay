#!/bin/bash

set -e

echo "Starting recording ..."

# Move to RecyclerXRay project dir
SCRIPT_DIR="$( cd "$(dirname "$0")" >/dev/null 2>&1 ; pwd -P )"
PROJECT_DIR=$(dirname "$SCRIPT_DIR")
cd "$PROJECT_DIR"

./gradlew executeScreenshotTests -Precord

echo "Screenshots are updated!"