# Linux 命令：
curl https://api.openai.com/v1/chat/completions \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $OPENAI_API_KEY" \
  -d '{
    "model": "gpt-3.5-turbo",
    "messages": [
      {
        "role": "system",
        "content": "You are a helpful assistant."
      },
      {
        "role": "user",
        "content": "Who won the world series in 2020?"
      },
      {
        "role": "assistant",
        "content": "The Los Angeles Dodgers won the World Series in 2020."
      },
      {
        "role": "user",
        "content": "Where was it played?"
      }
    ]
  }'

# Window 命令：
$headers = @{
    "Content-Type" = "application/json"
    "Authorization" = "Bearer $env:OPENAI_API_KEY"
}

$body = @"
{
    "model": "gpt-3.5-turbo",
    "messages": [
        {
            "role": "system",
            "content": "You are a helpful assistant."
        },
        {
            "role": "user",
            "content": "Who won the world series in 2020?"
        },
        {
            "role": "assistant",
            "content": "The Los Angeles Dodgers won the World Series in 2020."
        },
        {
            "role": "user",
            "content": "Where was it played?"
        }
    ]
}
"@

$response = Invoke-WebRequest -Uri https://api.openai.com/v1/chat/completions -Method Post -Headers $headers -Body $body -ContentType "application/json"

$response.Content
