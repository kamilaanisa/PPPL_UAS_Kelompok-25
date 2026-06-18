@profile
Feature: User Profile
  Pengujian halaman profil pengguna
  Fitur ini digunakan untuk memastikan pengguna dapat melihat
  dan memperbarui informasi profil mereka dengan benar

  Background:
    Given user is already logged in to the system
    And user navigates to the profile page
#
  Scenario: User views profile information
    Then user should see their username
    And user should see their registered email

  Scenario Outline: User updates profile details with various inputs
    When user updates their name to "<new_name>"
    And user updates their phone number to "<new_phone>"
    And user clicks the save button
    Then user should see a "<status_message>"
    And user should see their profile username updated to "<new_name>"


    Examples:
      | new_name      | new_phone    | status_message                    |
      | Budi Santoso  | 08123456789  | Profile berhasil diperbarui       |


  Scenario Outline: User fails to update profile with invalid inputs
    When user updates their name to "<new_name>"
    And user updates their phone number to "<new_phone>"
    And user clicks the save button
    Then user should see a "<status_message>"


    Examples:
      | new_name     | new_phone | status_message                   |
      |           | 08975527296  | Username tidak boleh kosong       |
      | july          | abcde$       | Nomor telepon tidak boleh mengandung huruf  |
      | andi          | 08755        | Nomor telepon minimal 10 digit              |

    @update-password
    Scenario Outline: user  updates their password
      When user updates their password with current password "<current_pwd>", new password "<new_pwd>", and confirmation "<confirmation_pwd>"
      And user clicks the save button
      Then user should see a "<status_message>"


      Examples:
        | current_pwd | new_pwd    | confirmation_pwd | status_message |
        | indonesia   | larasati123 | 123456789 | Konfirmasi password baru tidak cocok|
        | indonesia   | larasati123 | larasati123 | Profile berhasil diperbarui|
