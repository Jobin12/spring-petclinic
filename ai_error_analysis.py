from anthropic import AnthropicBedrock
import os
import re

client = AnthropicBedrock(
    aws_access_key=os.environ['AWS_ACCESS_KEY'],
    aws_secret_key=os.environ['AWS_SECRET_KEY'],
    aws_region='us-east-1'
)

log_file = open("pipeline.log", "r")
log = log_file.read()
log_file.close()


message = client.messages.create(
    model="anthropic.claude-v2",
    max_tokens=256,
    messages=[{"role": "user", "content": f"Summary the error and fix for the following jenkins log:\n{log}"}]
)

response = message.content[0].text
output = re.sub(r"\n\n+", "\n", response)

analysis_file = open('ai_analysis.html', 'a')
analysis_file.write("<h1>AI Error Analysis<h1><br>")
analysis_file.write(output)
analysis_file.close() 