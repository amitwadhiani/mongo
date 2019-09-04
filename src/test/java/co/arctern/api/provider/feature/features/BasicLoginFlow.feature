Feature: Test Login Flow APIs

# token generation via same scenario file.
  Scenario: Generate token and Fetch all users ( only admin )
    Given url 'https://provider.staging.arctern.co/oauth/token'
    And request "username=9999999999&password=111111&grant_type=password"
    And header Content-Type = 'application/x-www-form-urlencoded'
    And header Authorization = 'Basic TFJMNlpDRXQ1QWc3d2hhYTpMd3BycmpkTHpNYzl6U3E0WFk0aldDejNoTldZTVZWNg=='
    When method POST
    Then status 200
    And def accessToken = response.access_token
    Given url 'https://provider.staging.arctern.co/api/v1/provider/user/fetch-all'
    And header Authorization = 'Bearer ' +  accessToken
    When method GET
    Then status 200

  # token generation through separate scenario file.
  # use callonce for calling function only once and enable internal caching.
  # use call elsewhere.
  Scenario: Fetch user via token
    Given url 'https://provider.staging.arctern.co/api/v1/provider'
    And def res = callonce read('AdminToken.feature')
    And header Authorization = 'Bearer ' +  res.accessToken
    When method GET
    Then status 200

  Scenario: Fetch all users via admin login
    Given url 'https://provider.staging.arctern.co/api/v1/provider/users'
    And def res = callonce read('AdminToken.feature')
    And header Authorization = 'Bearer ' +  res.accessToken
    When method GET
    Then status 200
