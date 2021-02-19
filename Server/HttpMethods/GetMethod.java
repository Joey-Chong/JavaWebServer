package Server.HttpMethods;

public class GetMethod extends HttpMethod{
    @Override
    public void execute() {
        System.out.println("Creating instance GET");
    }

    @Override
    public String getStatusCode() {
        return null;
    }
}
