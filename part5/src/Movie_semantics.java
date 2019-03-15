import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

import org.apache.jena.ontology.OntModel;
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
	   model.read("part2.rdf");
	   
	    /*String file="myOwl.owl";
	    File f=new File(file);
	    FileReader r = null;
		try {
			r = new FileReader(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    OntModel model=ModelFactory.createOntologyModel();
	    model.read(r,null);*/
	    
		/*String queryString = "PREFIX ex: <http://www.semanticweb.org/dauvissat/ontologies/2019/2/untitled-ontology-3#> \n" +
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
				"PREFIX owl: <http://www.w3.org/2002/07/owl#>  \n" + 
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>  \n" + 				 
				"SELECT DISTINCT ?name WHERE {\n" + 
				"    ?name rdf:type owl:Actor \n" + 
				"}";*/	   
	   
	   
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
      
	   String queryString = "PREFIX ex:      <http://sample.com/facts/> \n" + 
		   		"PREFIX prop:      <http://sample.com/prop/> \n" + 
		   		"SELECT DISTINCT ?mName \n" + 
		   		"WHERE \n" + 
		   		" { \n";

	   queryString += getIncludedRows(includedActors, "Actor");
	   queryString += getIncludedRows(includedDirectors, "Director");
	   queryString += getIncludedRows(includedGenres, "Genre");
	   queryString += "  [] prop:isActorOf ?movie . \n ?movie prop:name ?mName . \n ";
		
	   queryString += getExcludedRows(excludedActors, "Actor");
	   queryString += getExcludedRows(excludedDirectors, "Director");
	   queryString += getExcludedRows(excludedGenres, "Genre");
	   
		   queryString += "   \n }";
		   //System.out.println(queryString);
		   Query query = QueryFactory.create(queryString) ;
		   System.out.print("Your films are: ");
		   try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
		     ResultSet results = qexec.execSelect() ;
		     boolean premier = true;
		     for ( ; results.hasNext() ; )
		     {
		    	 if(!premier) {
		    		 System.out.print(", ");
		    	 }
		       QuerySolution soln = results.nextSolution() ;
		       RDFNode x = soln.get("?mName") ;       // Get a result variable by name.
		       System.out.print(x);
		       premier = false;
		     }
		     if(premier) {
		    	 System.out.print("no such film");
		     }
		   }
  		 	System.out.print(".");
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
   
   public static String getIncludedRows(ArrayList<String> personArray, String personType) {
	      String rowsString = "";
	   	  for (int i = 0; i < personArray.size(); i++) {
	    	  rowsString += "   ?s" + i + personType + " prop:name \"" + personArray.get(i) + "\" . \n   ?s" + i + personType + " prop:is" + personType + "Of ?movie . \n";
	      }
	      return rowsString;
   }
   
   public static String getExcludedRows(ArrayList<String> personArray, String personType) {
	      String rowsString = "";
	   	  for (int i = 0; i < personArray.size(); i++) {
	    	  rowsString += " FILTER not exists { ?d" + i + personType + " prop:name \"" + personArray.get(i) + "\" . \n   ?d" + i + personType + " prop:is" + personType + "Of ?movie . \n } \n";
	      }
	      return rowsString;
   }
}