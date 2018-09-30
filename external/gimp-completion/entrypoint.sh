#!/usr/bin/env bash
mkdir -p /app/inputs
mkdir -p /app/masks
mkdir -p /app/results
export PYTHONPATH=$PYTHONPATH:/app
gimp -i --batch-interpreter python-fu-eval --batch - < completion_server.py