import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.util.FileManager;


public class Java2{
	
	public static void main(String args[])
	{
		sparqlQuery();
	}
	
	static void sparqlQuery()
	{
		FileManager.get().addLocatorClassLoader(Java2.class.getClassLoader());
		Model model = FileManager.get().loadModel("./data/Protege1.rdf");

		String content = null;
		try {
			content = new String(Files.readAllBytes(Paths.get("./data/QueryJava2.txt")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String queryString = content;
		
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		try {
			ResultSet results = qexec.execSelect();
			while (results.hasNext()) {
				QuerySolution soln = results.nextSolution();
				RDFNode x = soln.get("?x") ;
				System.out.println(x);
			}
		} finally {
			qexec.close();
		}
		
	}
}