package util;

import java.util.Random;
import java.util.UUID;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TestDataGenerator {


  //@Test
  void generateCustomerDatabaseInserts() {
    String[] firstNames = {"John", "Jane", "Michael", "Emily", "Chris", "Sarah", "David", "Laura",
        "James", "Linda",
        "Robert", "Patricia", "Joseph", "Barbara", "Thomas", "Jessica", "Mark", "Elizabeth",
        "Steven", "Karen"};
    String[] lastNames = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller",
        "Davis", "Rodriguez", "Martinez",
        "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson", "Taylor", "Moore", "Jackson",
        "Martin", "Lee"};
    String[] emails = {"example.com", "gmail.com", "outlook.com", "mixmail.com", "yahoo.com",
        "agilemonkeys.com"};

    Random random = new Random();

    for (int i = 1; i <= 100; i++) {
      String firstName = firstNames[random.nextInt(firstNames.length)];
      String lastName = lastNames[random.nextInt(lastNames.length)];
      String email = emails[random.nextInt(emails.length)];

      String randomEmail =
          firstName.toLowerCase() + random.nextInt(3000) + "." + lastName.toLowerCase()
              + random.nextInt(3000) + "@"
              + email.toLowerCase();
      String phoneNumber = "6" + (10000000 + random.nextInt(90000000));

      // Generate SQL INSERT statement
      String sql = String.format(
          "INSERT INTO customer (sk, first_name, last_name, email, phone_number, picture_name, picture_bytes, created_by, creation_date, last_modified_by, last_modified_date) "
              +
              "VALUES (nextval('customer_seq'), '%s', '%s', '%s', '%s', 'picture_%d.jpg', decode('"
              + UUID.randomUUID().toString().replaceAll("-", "")
              + "', 'hex'), 'TESTDATA', NOW() - INTERVAL '%d day', 'SystemUser', NOW() - INTERVAL '%d day');",
          firstName, lastName, randomEmail, phoneNumber, i, i, i
      );

      // Output the generated SQL statement
      System.out.println(sql);
    }
  }


  //@Test
  void generateUserDatabaseInserts() {
    StringBuilder sqlInserts = new StringBuilder();
    Random random = new Random();

    // Define roles
    String[] roles = {"ROLE_ADMIN", "ROLE_USER"};

    // Users generation
    for (int i = 1; i <= 30; i++) {
      String username = "user" + i;
      String password = generatePassword();
      String fullName = "User Full Name " + i;
      String createdBy = "admin";
      String creationDate = "NOW()";
      String lastModifiedBy = "admin";
      String lastModifiedDate = "NOW()";

      // Insert user with nextval('user_seq') for sk
      sqlInserts.append(String.format(
          "INSERT INTO public.\"user\" (sk, username, password, full_name, created_by, creation_date, last_modified_by, last_modified_date) "
              +
              "VALUES (nextval('user_seq'), '%s', '%s', '%s', '%s', %s, '%s', %s);\n",
          username, password, fullName, createdBy, creationDate, lastModifiedBy, lastModifiedDate));

      // Assign a unique role to each user randomly
      String role = roles[random.nextInt(roles.length)];
      // Add the user to the user_roles table with the assigned role
      sqlInserts.append(String.format(
          "INSERT INTO public.user_roles (user_sk, role_sk) VALUES (currval('user_seq'), %d);\n",
          getRoleSk(role)));
    }

    System.out.println(sqlInserts);
  }

  private String generatePassword() {
    return UUID.randomUUID().toString().replaceAll("-", "");
  }

  private int getRoleSk(String role) {
    return "ROLE_ADMIN".equals(role) ? 1 : 2;
  }

}
