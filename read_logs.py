import requests
from requests.auth import HTTPBasicAuth
import os

BUILD_URL = os.environ["BUILD_URL"]
USER = "admin"
PASSWORD = "password"
OUTPUT_FILE = "pipeline_logs.txt"

# Jenkins build log URL
log_url = f"{BUILD_URL}/consoleText"

# Fetch pipeline logs
response = requests.get(log_url, auth=HTTPBasicAuth(USER, PASSWORD))

if response.status_code == 200:
    print("Writing pipeline logs to file...")
    with open(OUTPUT_FILE, "w") as file:
        file.write(response.text)
    print(f"Logs successfully written to {OUTPUT_FILE}")
else:
    print(f"Failed to fetch logs. Status code: {response.status_code}")
