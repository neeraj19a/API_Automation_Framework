package utilities;

public class APIConstant {

    public static class ApiMethods {
        public static String POST = "POST";
        public static String GET = "GET";
        public static String DELETE = "DELETE";
        public static String PUT = "PUT";


    }

    public static class APIPaths {
        public static final String GET_LISTS_OF_POSTS = "/posts";
        public static final String GET_SINGLE_POSTS = "/posts/{postId}";
        public static final String GET_LIST_OF_USERS = "/api/unknown";
        public static final String POST_USER = "/api/users";

    }
}