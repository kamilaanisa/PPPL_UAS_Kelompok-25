@login
Feature: User Login
    Pengujian halaman login company profile dokter hewan fanina
    Fitur ini digunakan untuk memastikan bahwa pengguna dapat masuk
    kedalam sistem menggunakan akun yang valid

    Scenario Outline: User login dengan berbagai jenis akun
        Given user is on the home page
        When user clicks the login button on home page
        And user enters username "<username>" and password "<password>"
        And user submits the login form
        Then user should see page path as "<expected_result>"

        Examples:
            | username                   | password     | expected_result                                     |
            | admin@klinikdrfanina.com   | password123  | https://compro-drhfanina-pad1.vercel.app/dashboard  |
            | admin@klinikdrfanina.com   | 11111111111  | These credentials do not match our records. |



