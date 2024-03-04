# 画图 API - Mac 电脑可直接运行，Windows 电脑放到 cmd 里执行，或者放到 ApiPost 里导入 curl 再执行
# Linux 命令：
curl --location --request POST 'https://service-d6wuqy4n-1320869466.cd.apigw.tencentcs.com/v1/images/generations' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer sk-cxFtovolwcSmPElG5867C634010448AfA6Eb2b942d3a2aDc' \
--header 'Accept: */*' \
--header 'Connection: keep-alive' \
--data-raw '{
    "model": "dall-e-3",
    "prompt": "美丽的风景画",
    "n": 1,
    "size": "1024x1024"
}'

# Window 命令：
$headers = @{
    'Content-Type'  = 'application/json'
    'Authorization' = 'Bearer sk-cxFtovolwcSmPElG5867C634010448AfA6Eb2b942d3a2aDc'
    'Accept'        = '*/*'
}

$body = @{
    'model'    = 'dall-e-3'
    'prompt'   = '美丽的风景画'
    'n'        = 1
    'size'     = '1024x1024'
} | ConvertTo-Json

$response = Invoke-WebRequest -Uri 'https://service-d6wuqy4n-1320869466.cd.apigw.tencentcs.com/v1/images/generations' -Method POST -Headers $headers -Body $body

$response.Content
