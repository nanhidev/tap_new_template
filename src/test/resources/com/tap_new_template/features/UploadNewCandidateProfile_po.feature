
@feature_upload_new_candidate_profile
Feature: UploadNewCandidateProfile_po

Background: 
  Given the user is on the Profile section

@valid-upload
Scenario Outline: Upload New Candidate Profile
  When the user clicks on the upload icon
  And the user browses and selects a valid candidate profile file
  And the user clicks on the Upload button
  Then the upload process should complete without errors
  And a success confirmation message should appear, indicating the profile was uploaded successfully

Examples:
  | candidate_profile_file |
  | valid_candidate_profile.pdf |
  | valid_candidate_profile.docx |

@valid-upload-first-profile
Scenario Outline: Upload New Candidate Profile
  When the recruiter clicks on the upload icon
  And the recruiter browses and selects the <candidate_profile_file>
  And the recruiter clicks on the Upload button
  Then a confirmation message should appear for the <candidate_profile_file> upload
  And both profiles should be listed in the Profile section

Examples:
  | candidate_profile_file               |
  | first valid candidate profile file    |
  | second valid candidate profile file   |

  @feature_upload_candidate_profile
  @upload-profile
  Scenario Outline: Upload New Candidate Profile
    When the user clicks on the upload (cloud) icon
    And the user browses and selects a candidate profile file in a supported format
    And the user clicks on the Upload button
    And the user waits for the upload process to complete
    Then a success confirmation message appears, indicating the profile was uploaded successfully

  Examples:
    | candidate_profile_file |
    | valid_candidate_profile.pdf |
    | valid_candidate_profile.docx |

  @valid-upload
  Scenario Outline: Upload New Candidate Profile
    When the user clicks on the upload (cloud) icon
    And the user browses and selects a candidate profile file with a long filename <filename>
    And the user clicks on the Upload button
    And the user waits for the upload process to complete
    Then the success confirmation message should appear, indicating the profile was uploaded successfully

    Examples:
      | filename                                                       |
      | This_is_a_very_long_filename_for_a_candidate_profile_document.pdf |

@upload-candidate-profile
Scenario Outline: Upload New Candidate Profile
  When the user clicks on the upload icon
  And the user browses and selects a candidate profile file with metadata
  And the user clicks the Upload button
  And the user waits for the upload process to complete
  Then the metadata should be displayed correctly in the Profile section

Examples:
  | candidate_profile_file |
  | <candidate_profile_file> |

  @valid-file-upload
  Scenario Outline: Upload New Candidate Profile
    When the user clicks on the upload (cloud) icon
    And the user browses and selects a candidate profile file with a valid extension
    And the user clicks on the Upload button
    And the user waits for the upload process to complete
    Then the success confirmation message should appear, indicating the profile was uploaded successfully

    Examples:
      | file_extension |
      | RTF            |

  @feature_upload_new_candidate_profile
  @upload-success
  Scenario Outline: Upload New Candidate Profile
    Given the user is in the Profile section
    When the user clicks on the upload (cloud) icon
    And the user browses and selects a valid candidate profile file
    And the user clicks on the Upload button
    And the user waits for the upload process to complete
    Then a success confirmation message should be displayed, indicating the profile was uploaded successfully

  Examples:
    | valid_candidate_profile_file |
    | <valid_candidate_profile_file> |

  @valid-upload
  Scenario Outline: Upload New Candidate Profile
    When the user clicks on the upload icon
    And the user browses and selects a candidate profile file in a different format
    And the user clicks on the Upload button
    And the user waits for the upload process to complete
    Then the user should see a success confirmation message indicating the profile was uploaded successfully

  Examples:
    | candidate_profile_file |
    | <candidate_profile_file> |

@upload-candidate-profiles
Scenario Outline: Upload New Candidate Profile
  When the Hiring Manager clicks on the upload (cloud) icon
  And the file selection dialog opens
  And the Hiring Manager selects multiple valid candidate profile files
  And the Hiring Manager clicks on the Upload button
  And the upload process initiates without errors for all files
  Then all profiles should be listed in the Profile section after upload

Examples:
  | <file1>          | <file2>          |
  | valid_profile_1  | valid_profile_2  |
