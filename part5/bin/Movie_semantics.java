import java.util.*;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

 
class Movie_semantics
{
   public static void main(String args[])
   {
	   
	   Model model = ModelFactory.createDefaultModel() ;
	   model.read("myOwl.owl");
	   String queryString = "SELECT ?name \r\n";
	   Query query = QueryFactory.create(queryString) ;
	   try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
	     ResultSet results = qexec.execSelect() ;
	     for ( ; results.hasNext() ; )
	     {
	       QuerySolution soln = results.nextSolution() ;
	       RDFNode x = soln.get("varName") ;       // Get a result variable by name.
	       Resource r = soln.getResource("VarR") ; // Get a result variable - must be a resource
	       Literal l = soln.getLiteral("VarL") ;   // Get a result variable - must be a literal
	     }
	   }
	   
	   
	   
      ArrayList<String> includedActors = new ArrayList<String>();
      ArrayList<String> excludedActors = new ArrayList<String>();
      ArrayList<String> includedDirectors = new ArrayList<String>();
      ArrayList<String> excludedDirectors = new ArrayList<String>();
      ArrayList<String> includedGenres = new ArrayList<String>();
      ArrayList<String> excludedGenres = new ArrayList<String>();
      Scanner in = new Scanner(System.in);
      while(readPerson(includedActors, in, "included", "actor"));
      while(readPerson(excludedActors, in, "excluded", "actor"));
      while(readPerson(includedDirectors, in, "included", "director"));
      while(readPerson(excludedDirectors, in, "excluded", "director"));
      while(readPerson(includedGenres, in, "included", "genre"));
      while(readPerson(excludedGenres, in, "excluded", "genre"));
   }
   
   //returns whether the user wants to add more
   public static boolean readPerson(ArrayList<String> personArray, Scanner in, String inclusion, String personType)
   {
	  String myInput;
	  if(personArray.size() == 0) {
		  System.out.println("Enter a name of a " + personType + " you wants to be " + inclusion + " in the movie: (if none enter 'n')");
	  } else {
	      System.out.print("Do you want to add an other " + inclusion + " " + personType + " besides " );
	      for (int i = 0; i < personArray.size(); i++) {
	    	  if (i != 0) System.out.print(", ");
	          System.out.print(personArray.get(i));
	      }
	      System.out.println("? (if yes, enter his/her name, if not, enter 'n')");		  
	  }
	  myInput = in.nextLine();
      if("n".equals(myInput)) {
    	  return false;
      } else {
    	  personArray.add(myInput);
          return true;
      }
   }
}