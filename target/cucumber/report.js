$(document).ready(function() {var formatter = new CucumberHTML.DOMFormatter($('.cucumber-report'));formatter.uri("Latest.feature");
formatter.feature({
  "line": 1,
  "name": "",
  "description": "Verify different GET operations using Rest-Assured",
  "id": "",
  "keyword": "Feature"
});
formatter.scenario({
  "line": 5,
  "name": "Verify GET Operation with Bearer Authentication Token",
  "description": "",
  "id": ";verify-get-operation-with-bearer-authentication-token",
  "type": "scenario",
  "keyword": "Scenario",
  "tags": [
    {
      "line": 4,
      "name": "@smoke4"
    }
  ]
});
formatter.step({
  "line": 6,
  "name": "I perform authentication operation for \"/auth/login\"with body",
  "rows": [
    {
      "cells": [
        "email",
        "password"
      ],
      "line": 7
    },
    {
      "cells": [
        "bruno@email.com",
        "bruno"
      ],
      "line": 8
    }
  ],
  "keyword": "Given "
});
formatter.step({
  "line": 9,
  "name": "I perform GET operation for \"/posts/1\"",
  "keyword": "Given "
});
formatter.step({
  "line": 10,
  "name": "I should see the author name as \"Neeraj\"",
  "keyword": "Then "
});
formatter.match({
  "arguments": [
    {
      "val": "/auth/login",
      "offset": 40
    }
  ],
  "location": "Latest.iPerformAuthenticationOperationForWithBody(String,DataTable)"
});
formatter.result({
  "duration": 6094273200,
  "error_message": "java.lang.NullPointerException\r\n\tat steps.Latest.iPerformAuthenticationOperationForWithBody(Latest.java:46)\r\n\tat ✽.Given I perform authentication operation for \"/auth/login\"with body(Latest.feature:6)\r\n",
  "status": "failed"
});
formatter.match({
  "arguments": [
    {
      "val": "/posts/1",
      "offset": 29
    }
  ],
  "location": "Latest.i_perform_GET_operation_for(String)"
});
formatter.result({
  "status": "skipped"
});
formatter.match({
  "arguments": [
    {
      "val": "Neeraj",
      "offset": 33
    }
  ],
  "location": "Latest.iShouldSeeTheAuthorNameAs(String)"
});
formatter.result({
  "status": "skipped"
});
formatter.scenario({
  "line": 13,
  "name": "Verify Delete Operation For Post",
  "description": "",
  "id": ";verify-delete-operation-for-post",
  "type": "scenario",
  "keyword": "Scenario",
  "tags": [
    {
      "line": 12,
      "name": "@smoke4"
    }
  ]
});
formatter.step({
  "line": 14,
  "name": "I perform POST operation for \"/posts\" with body as",
  "rows": [
    {
      "cells": [
        "id",
        "title",
        "author"
      ],
      "line": 15
    },
    {
      "cells": [
        "8",
        "Test automation",
        "Neeraj"
      ],
      "line": 16
    }
  ],
  "keyword": "Given "
});
formatter.step({
  "line": 17,
  "name": "I perform DELETE operation for \"/posts/{postId}\"",
  "rows": [
    {
      "cells": [
        "postId"
      ],
      "line": 18
    },
    {
      "cells": [
        "8"
      ],
      "line": 19
    }
  ],
  "keyword": "And "
});
formatter.step({
  "line": 20,
  "name": "I perform GET operation with Path Parameter for \"/posts/{postId}\"",
  "rows": [
    {
      "cells": [
        "postId"
      ],
      "line": 21
    },
    {
      "cells": [
        "8"
      ],
      "line": 22
    }
  ],
  "keyword": "And "
});
formatter.match({
  "arguments": [
    {
      "val": "/posts",
      "offset": 30
    }
  ],
  "location": "Latest.iPerformPOSTOperationForWithBodyAs(String,DataTable)"
});
formatter.result({
  "duration": 37568800,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "/posts/{postId}",
      "offset": 32
    }
  ],
  "location": "Latest.iPerformDELETEOperationFor(String,DataTable)"
});
formatter.result({
  "duration": 87865000,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "/posts/{postId}",
      "offset": 49
    }
  ],
  "location": "Latest.iPerformGETOperationWithPathParameterFor(String,DataTable)"
});
formatter.result({
  "duration": 31347800,
  "status": "passed"
});
formatter.scenario({
  "comments": [
    {
      "line": 24,
      "value": "#Then I should not see the title as \"Test automation\""
    }
  ],
  "line": 28,
  "name": "Verify Post Operation For Profile",
  "description": "",
  "id": ";verify-post-operation-for-profile",
  "type": "scenario",
  "keyword": "Scenario",
  "tags": [
    {
      "line": 27,
      "name": "@smoke4"
    }
  ]
});
formatter.step({
  "line": 29,
  "name": "I perform POST operation for \"/posts/{profileNo}/profile\" with body",
  "rows": [
    {
      "cells": [
        "name",
        "profile"
      ],
      "line": 30
    },
    {
      "cells": [
        "Neeraj",
        "12"
      ],
      "line": 31
    }
  ],
  "keyword": "Given "
});
formatter.step({
  "line": 32,
  "name": "I should see the body name as \"Neeraj\"",
  "keyword": "Then "
});
formatter.match({
  "arguments": [
    {
      "val": "/posts/{profileNo}/profile",
      "offset": 30
    }
  ],
  "location": "Latest.iPerformPOSTOperationForWithBody(String,DataTable)"
});
formatter.result({
  "duration": 25135100,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "Neeraj",
      "offset": 31
    }
  ],
  "location": "Latest.iShouldSeeTheBodyNameAs(String)"
});
formatter.result({
  "duration": 46191000,
  "status": "passed"
});
formatter.scenario({
  "line": 36,
  "name": "Verify PUT Operation For Post",
  "description": "",
  "id": ";verify-put-operation-for-post",
  "type": "scenario",
  "keyword": "Scenario",
  "tags": [
    {
      "line": 35,
      "name": "@smoke4"
    }
  ]
});
formatter.step({
  "line": 37,
  "name": "I perform POST operation for \"/posts\" with body as",
  "rows": [
    {
      "cells": [
        "id",
        "title",
        "author"
      ],
      "line": 38
    },
    {
      "cells": [
        "11",
        "Test automation",
        "Neeraj"
      ],
      "line": 39
    }
  ],
  "keyword": "Given "
});
formatter.step({
  "line": 40,
  "name": "I perform PUT operation for \"/posts/{postId}\"",
  "rows": [
    {
      "cells": [
        "postId",
        "title",
        "author"
      ],
      "line": 41
    },
    {
      "cells": [
        "11",
        "Testing",
        "Neeraj"
      ],
      "line": 42
    }
  ],
  "keyword": "And "
});
formatter.step({
  "line": 43,
  "name": "I perform GET operation with Path Parameter for \"/posts/{postId}\"",
  "rows": [
    {
      "cells": [
        "postId"
      ],
      "line": 44
    },
    {
      "cells": [
        "11"
      ],
      "line": 45
    }
  ],
  "keyword": "And "
});
formatter.step({
  "line": 47,
  "name": "I \"should not\" see the title as \"Test automation \"",
  "keyword": "Then "
});
formatter.match({
  "arguments": [
    {
      "val": "/posts",
      "offset": 30
    }
  ],
  "location": "Latest.iPerformPOSTOperationForWithBodyAs(String,DataTable)"
});
formatter.result({
  "duration": 22471000,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "/posts/{postId}",
      "offset": 29
    }
  ],
  "location": "Latest.iPerformPUTOperationFor(String,DataTable)"
});
formatter.result({
  "duration": 22857900,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "/posts/{postId}",
      "offset": 49
    }
  ],
  "location": "Latest.iPerformGETOperationWithPathParameterFor(String,DataTable)"
});
formatter.result({
  "duration": 22007300,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "should not",
      "offset": 3
    },
    {
      "val": "Test automation ",
      "offset": 33
    }
  ],
  "location": "Latest.iShouldNotSeeTheTitleAs(String,String)"
});
formatter.result({
  "duration": 13783000,
  "status": "passed"
});
});