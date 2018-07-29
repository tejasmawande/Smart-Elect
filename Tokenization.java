package Method;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import javax.swing.JOptionPane;
import java.io.BufferedReader;
import java.io.FileReader;

public class Tokenization
{
    public static int neg,pos,nue;
    public  static ArrayList<String> array1 = new ArrayList<>();    //unknown word occuring in positive comment
    public  static ArrayList<String> array2 = new ArrayList<>();    //unknown word apperaing in negative comment
    public  static ArrayList<String> array3 = new ArrayList<>();    //All unknown words
    public  static  float c,Score,scor_r,scor_p,scor_s,scor_w,s1,s2,s3,s4,s5,s6,s7,s8,s9,s10,s11,s12,s13,s14,s15,s16,scor_rr,scor_ss,scor_pp,scor_ww,scor_rrr,scor_sss,scor_ppp,scor_www,scor_rrrr,scor_ssss,scor_pppp,scor_wwww;
    public static  ArrayList<String> list1 = new ArrayList<>();      //Tokens in comment without stopwords
    public static  ArrayList<String> list2 = new ArrayList<>();     //contains stopwords
    public static  ArrayList<String> list3 = new ArrayList<>();     //contains opinionwords
    public static  ArrayList<Float> list4 = new ArrayList<Float>();  //contains score of opinion words
    public static  ArrayList<String> list10 = new ArrayList<>();    //contains all tokens of comment
    public static  ArrayList<String> nwords = new ArrayList<>();    //contains negation words
    public static  ArrayList<String> iwords = new ArrayList<>();    // contains intensifier words
    public static  Hashtable< Integer, String > hash = new Hashtable< Integer, String >();  //contains all words of dictionary
    public static  float pprob=0.0f,nprob=0.0f,unpprob=0.0f,unnprob=0.0f,scorep=0.0f,scoren=0.0f,sun=0.0f,sup=0.0f;     
    public static  String query1="insert into candidate_score(sr_no,Roadlines_score,sanitization_score,publictransport_score,watersupply_score,total_score)values(?,?,?,?,?,?)";
    public static  String query2="insert into candidate2_score(sr_no,Roadlines_score,sanitization_score,publictransport_score,watersupply_score,total_score)values(?,?,?,?,?,?)";
    public static  String query3="insert into candidate3_score(sr_no,Roadlines_score,sanitization_score,publictransport_score,watersupply_score,total_score)values(?,?,?,?,?,?)";
    public static  String query4="insert into candidate4_score(sr_no,Roadlines_score,sanitization_score,publictransport_score,watersupply_score,total_score)values(?,?,?,?,?,?)";
    public static  String query5="select * from candidate_score"; 
    public static  String query6="select * from candidate2_score"; 
    public static  String query7="select * from candidate3_score"; 
    public static  String query8="select * from candidate4_score";
    public static  void main(String[] args)throws IOException , ClassNotFoundException 
    {
        Connection con = null;
          //creates arraylists
        
        try 
        {
            Class.forName("com.mysql.jdbc.Driver");     
            String url = "jdbc:mysql://localhost:3306/sentiment_analysis";  //database path 
            
            con = DriverManager.getConnection(url, "root", "mysql");    //connection created with username and password
      
            String sql = "select * from candidate_one";   //comments for Candidate one
            String sql1 = "select * from stopwords";
            String sql2 = "select * from opinionwordss";
            String sql3 = "select * from intensifierwords";
            String sql4 = "select * from negationwords";
            String sql5 = "select * from candidate_two";   //comments for candidate two
            String sql6 = "select * from candidate_three";  //comments for candidate three
            String sql7 = "select * from candidate_four";  //comments for candidate four
            
            Statement st = con.createStatement();
            Statement st1 = con.createStatement();
            Statement st2 = con.createStatement();
            Statement st3 = con.createStatement();
            Statement st4 = con.createStatement();      //represents a sql statement
            Statement st5 = con.createStatement();
            Statement st6 = con.createStatement();
            Statement st7 = con.createStatement();

           
            ResultSet rs = st.executeQuery(sql);
            ResultSet rs1 = st1.executeQuery(sql1);
            ResultSet rs2 = st2.executeQuery(sql2);
            ResultSet rs3 = st3.executeQuery(sql3);
            ResultSet rs4 = st4.executeQuery(sql4); //   result sets
            ResultSet rs5 = st5.executeQuery(sql5);
            ResultSet rs6 = st6.executeQuery(sql6);
            ResultSet rs7 = st7.executeQuery(sql7);

            BufferedReader rd = new BufferedReader( new FileReader ("D:\\dictionary.txt"));
            
            String line;
            int i = 0;
         
            
            while ((line = rd.readLine()) != null)
            {
                hash.put(i, line);
                i++;
            }
            
           
            float Score=0;
            
            while(rs4.next())
            {
                String negativewords = ""+rs4.getString(1);
                nwords.add(negativewords);
            }
           
            while(rs3.next())
            {
                String intensifier = ""+rs3.getString(1);
                iwords.add(intensifier);   
            }
            
            while(rs2.next())
            {
                String opverb = ""+rs2.getString(1);
                list3.add(opverb);                                  //list3 == opinionverbs
                float sc = rs2.getFloat("score");
                list4.add(sc);                                      //list4 ==score of opinionwords
            }
            
            while(rs1.next())
            {
                String StopWord = ""+rs1.getString(1);              //list2 ==  stopwords
                list2.add(StopWord);
            }
            int l=1;
            while(rs.next())
            {
                String Roadlines = ""+rs.getString(2);
                StringTokenizer c1 = new StringTokenizer(Roadlines," ");
                s1=cal_score(c1);                   //calculates scoreof c1 for roadlines
                scor_r+=s1;   
            }
               float UNK1=unk(array1,array2,array3,pos,neg,scor_r);
               System.out.println("Score of candidate1 for roadlines is :: "+UNK1);
               array1.clear();
               array2.clear();
               array3.clear();
               pos=0;
               neg=0;
               rs.beforeFirst();

            while(rs.next())
            {
                String sanitization = ""+rs.getString(3);
                StringTokenizer c2 = new StringTokenizer(sanitization," ");
                s2=cal_score(c2);
                scor_s+=s2;
            }
            float UNK2=unk(array1,array2,array3,pos,neg,scor_s);
            System.out.println("Score of candidate1 for sanitization is :: "+UNK2);
            array1.clear();
            array2.clear();
            array3.clear();
            pos=0;
            neg=0;
            rs.beforeFirst();            
            while(rs.next())
            {
                String publictransport = ""+rs.getString(4);
                StringTokenizer c3 = new StringTokenizer(publictransport," ");
                s3=cal_score(c3);
                scor_p+=s3;                
            }
            float UNK3=unk(array1,array2,array3,pos,neg,scor_p);
            System.out.println("Score of candidate1 for publictransport is :: "+UNK3);   
            array1.clear();
            array2.clear();
            array3.clear();
            pos=0;
            neg=0;
            rs.beforeFirst();
            while(rs.next())
            {
                String watersupply = ""+rs.getString(5);
                StringTokenizer c4 = new StringTokenizer(watersupply," ");
                s4=cal_score(c4);
                scor_w+=s4;
                        
            }
                float UNK4=unk(array1,array2,array3,pos,neg,scor_w);
                System.out.println("Score of candidate1 for watersupply is :: "+UNK4);    
                array1.clear();
                array2.clear();
                array3.clear();
                pos=0;
                neg=0;
                System.out.println("CANDIDATE 2:::::::::::::::::::::");
                while(rs5.next())
                {
                    String Roadlines1 = ""+rs5.getString(2);
                    StringTokenizer c5 = new StringTokenizer(Roadlines1," ");
                    s5=cal_score(c5);
                    scor_rr+=s5;
                }
                float UNK5=unk(array1,array2,array3,pos,neg,scor_rr);
                System.out.println("Score of candidate2 for roadlines is :: "+UNK5);
                array1.clear();
                array2.clear();
                array3.clear();
                pos=0;
                neg=0;
                rs5.beforeFirst();

                while(rs5.next())
                {
                    String sanitization1 = ""+rs5.getString(3);
                    StringTokenizer c6 = new StringTokenizer(sanitization1," ");
                    s6=cal_score(c6);
                    scor_ss+=s6;
                }
                float UNK6=unk(array1,array2,array3,pos,neg,scor_ss);
                System.out.println("Score of candidate2 for sanitization is :: "+UNK6);
                array1.clear();
                array2.clear();
                array3.clear();
                pos=0;
                neg=0;
                rs5.beforeFirst();
                while(rs5.next())
                {
                    String publictransport1 = ""+rs5.getString(4);
                    StringTokenizer c7 = new StringTokenizer(publictransport1," ");
                    s7=cal_score(c7);
                    scor_pp+=s7;        
                }
                float UNK7=unk(array1,array2,array3,pos,neg,scor_pp);
                System.out.println("Score of candidate2 for publictransport is :: "+UNK7); 
                array1.clear();
                array2.clear();
                array3.clear();
                pos=0;
                neg=0;
                rs5.beforeFirst();
                while(rs5.next())
                {
                    String watersupply1 = ""+rs5.getString(5);
                    StringTokenizer c8 = new StringTokenizer(watersupply1," ");
                    s8=cal_score(c8);
                    scor_ww+=s8;         
                }
                float UNK8=unk(array1,array2,array3,pos,neg,scor_ww);
                System.out.println("Score of candidate2 for watersupply is :: "+UNK8);
                array1.clear();
                array2.clear();
                array3.clear();
                pos=0;
                neg=0;
                System.out.println("CANDIDATE 3:::::::::::::::::::::");

                while(rs6.next())
                {
                    String Roadlines2 = ""+rs6.getString(2);
                    StringTokenizer c9 = new StringTokenizer(Roadlines2," ");
                    s9=cal_score(c9);
                    scor_rrr+=s9; 
                }
                float UNK9=unk(array1,array2,array3,pos,neg,scor_rrr);
                System.out.println("Score of candidate3 for roadlines is :: "+UNK9);
                array1.clear();
                array2.clear();
                array3.clear();
                pos=0;
                neg=0;
                rs6.beforeFirst();
                while(rs6.next())
                {
                    String sanitization2 = ""+rs6.getString(3);
                    StringTokenizer c10 = new StringTokenizer(sanitization2," ");
                    s10=cal_score(c10);
                    scor_sss+=s10;
                }
                float UNK10=unk(array1,array2,array3,pos,neg,scor_sss);
                System.out.println("Score of candidate3 for sanitation is :: "+UNK10);
                array1.clear();
                array2.clear();
                array3.clear();
                pos=0;
                neg=0;
                rs6.beforeFirst();
                while(rs6.next())
                {
                    String publictransport2 = ""+rs6.getString(4);
                    StringTokenizer c11 = new StringTokenizer(publictransport2," ");
                    s11=cal_score(c11);
                    scor_ppp+=s11;
                }
                float UNK11=unk(array1,array2,array3,pos,neg,scor_ppp);
                System.out.println("Score of candidate3 for public Transport is :: "+UNK11);
                array1.clear();
                array2.clear();
                array3.clear();
                pos=0;
                neg=0;
                rs6.beforeFirst();
                while(rs6.next())
                {
                    String watersupply2 = ""+rs6.getString(5);
                    StringTokenizer c12 = new StringTokenizer(watersupply2," ");
                    s12=cal_score(c12);
                    scor_www+=s12;
                }
                float UNK12=unk(array1,array2,array3,pos,neg,scor_www);
                System.out.println("Score of candidate3 for watersupply is :: "+UNK12);
                array1.clear();
                array2.clear();
                array3.clear();
                pos=0;
                neg=0;
                System.out.println("CANDIDATE 4:::::::::::::::::::::");

                while(rs7.next())
                {
                    String Roadlines3 = ""+rs7.getString(2);
                    StringTokenizer c13 = new StringTokenizer(Roadlines3," ");
                    s13=cal_score(c13);
                    scor_rrrr+=s13; 
                }
                float UNK13=unk(array1,array2,array3,pos,neg,scor_rrrr);
                System.out.println("Score of candidate4 for roadlines is :: "+UNK13);
                array1.clear();
                array2.clear();
                array3.clear();
                pos=0;
                neg=0;
                rs7.beforeFirst();
                while(rs7.next())
                {
                    String sanitization3 = ""+rs7.getString(3);
                    StringTokenizer c14 = new StringTokenizer(sanitization3," ");
                    s14=cal_score(c14);
                    scor_ssss+=s14;
                }
                float UNK14=unk(array1,array2,array3,pos,neg,scor_ssss);
                System.out.println("Score of candidate4 for sanitation is :: "+UNK14);
                array1.clear();
                array2.clear();
                array3.clear();
                pos=0;
                neg=0;
                rs7.beforeFirst();
                while(rs7.next())
                {
                    String publictransport3 = ""+rs7.getString(4);
                    StringTokenizer c15 = new StringTokenizer(publictransport3," ");
                    s15=cal_score(c15);
                    scor_pppp+=s15;
                }
                float UNK15=unk(array1,array2,array3,pos,neg,scor_pppp);
                System.out.println("Score of candidate4 for public Transport is :: "+UNK15);
                array1.clear();
                array2.clear();
                array3.clear();
                pos=0;
                neg=0;
                rs7.beforeFirst();
                while(rs7.next())
                {
                    String watersupply3 = ""+rs7.getString(5);
                    StringTokenizer c16 = new StringTokenizer(watersupply3," ");
                    s16=cal_score(c16);
                    scor_wwww+=s16;
                }
                float UNK16=unk(array1,array2,array3,pos,neg,scor_wwww);
                System.out.println("Score of candidate4 for watersupply is :: "+UNK16);
                array1.clear();
                array2.clear();
                array3.clear();
                pos=0;
                neg=0;
                float cad1_total_sore=UNK1+UNK2+UNK3+UNK4;
                float cad2_total_sore=UNK5+UNK6+UNK7+UNK8;
                float cad3_total_sore=UNK9+UNK10+UNK11+UNK12;
                float cad4_total_sore=UNK13+UNK14+UNK15+UNK16;

                insert_score_in_cad1(UNK1,UNK2,UNK3,UNK4,cad1_total_sore,query1,query5);
                insert_score_in_cad1(UNK5,UNK6,UNK7,UNK8,cad2_total_sore,query2,query6);
                insert_score_in_cad1(UNK9,UNK10,UNK11,UNK12,cad3_total_sore,query3,query7);
                insert_score_in_cad1(UNK13,UNK14,UNK15,UNK16,cad4_total_sore,query4,query8);


          /* 
                     
            System.out.println("Unknown positive unkown words:"+array1);
            System.out.println("Unknown negative unknown words:"+array2);
            System.out.println("Unknown words:"+array3);
            System.out.println("Score of candidate is :: "+Score);*/
        } 
        catch (ClassNotFoundException e) 
        {
                // TODO Auto-generated catch block
                e.printStackTrace();
        }	//loading Driver
        catch (SQLException e) 
        {
                // TODO Auto-generated catch block
                e.printStackTrace();
        }
        finally
        {
                try {
                        if(con!=null)
                        con.close();
                } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
        }
    }
   public static float cal_score(StringTokenizer c)
   {
       while(c.hasMoreTokens())
                {
                    String b=c.nextToken();
                    if(hash.containsValue(b.toLowerCase()))
                    {
                        porter obj = new porter();
                        String a  = obj.stripAffixes(b);        //function of porter stemmer
                        list10.add(a.replaceAll("[^! && [^A-Z]+ && [^a-z]+]","#")); 
                    }
                }
                
                for(int ii=0;ii<list10.size();ii++)
                {
                    String z=list10.get(ii);
                    String w="";
                    for(int p=0;p<z.length();p++)
                    {
                        if(z.charAt(p)!='#')
                        {
                            w+=z.charAt(p);
                        }
                    }
                    list1.add(w);       
                }
                
                list10.clear();
                list1.removeAll(list2);
                
                for(int m=0;m<list1.size();m++)
                {
                    System.out.println(list1.get(m));
                }
                float scc=0;
                scc=score(list1,list2,list3,list4,iwords,nwords);
                list1.clear();
               // Score+=scc;
                return scc;
   }
    public static float score(ArrayList<String> list1,ArrayList<String> list2,ArrayList<String> list3,ArrayList<Float> list4,ArrayList<String>iwords,ArrayList<String> nwords)
    {
        float sco=0;                                           
        float t=1;
        ArrayList<String> list9=new ArrayList<String>();    //LIST 9 CONTAINS WORDS AFTER STEMMING
        boolean f =false;
        boolean f2 =false;
        boolean f1 =false;
        boolean f3 =false;
       
        for(int i=0;i<list1.size();i++)
        {
            f1=false;
            String a=list1.get(i);

            if(a.equals("!!!"))                //CHECKS IF COMMENT CONTAINS EXCLAIMATION MARKS
            {
                sco=sco+1;
            }
                       
            for(int k=0;k<nwords.size();k++)   
            {
                String n = nwords.get(k);
                if(a.equals(n))              //CHECKS IF COMMENT CONTAINS NEGATION WORDS
                {
                   t=-1;
                   f1=true;
                   break;
                }
            }
     
            int m;
            if(a.matches("[A-Z]+"))         //CHECKS IF COMMENT CONTAINS CAPITAL LETTERS
            {
                f2=true;
            }
            for(m=0;m<iwords.size();m++)
            {
                String h = iwords.get(m);

                if(a.toLowerCase().equals(h))   // CHECK IF COMMENT CONTAINS INTENSIFIER WORDS
                {
                    f=true;
                    f1=true;
                    break;
                }
            }   
            for(int j=0;j<list3.size();j++)
            { 
                String b=list3.get(j);
                if(a.toLowerCase().equals(b))
                {
                    float S=list4.get(j); 
                    if(f==true)
                    {
                        if(S>=1)            //// CHANGES THE SCORE OF INTENSIFIRE WORDS ACCORDINGLY
                        {
                            S=S+2;
                        }
                        else
                        {
                            S=S-2;
                        }
                        f=false;
                    }
                    if(f2==true)                    
                    {
                        if(S>=1)            // CHANGES THE SCORE OF CAPTIAL LETTERS ACCORDINGLY
                        {
                            S=S+5;
                        }
                        else
                        {
                            S=S-5;
                        }
                        f=false;
                    }

                    S=S*t;
                    sco=sco+S;
                    f1=true;
                    
                    break;
                           
                }
            }

            if(f1==false)
            {
                  list9.add(a);
            }

        }
        int q;    
            
        if(!list9.isEmpty())
        {
            for( q=0;q<list9.size();q++)
            {
                if(sco>=0)
                {
                    array1.add(list9.get(q));
                }
                else
                {
                    array2.add(list9.get(q));
                }
                if(!array3.contains(list9.get(q)))
                {
                    array3.add(list9.get(q));
                }
            }

            list9.clear();
        }      
        if(sco>=0)
        {
            pos++;
        }
        else if(sco<0)
        {
            neg++;
        }

        f2=false;
        f=false;

        return sco;
               
    }
      public static float unk(ArrayList<String> array1,ArrayList<String> array2,ArrayList<String> array3,int pos,int neg,float Score)
      {
           int totall=pos+neg;
            pprob=pos/(float)totall;
            nprob=neg/(float)totall;
        //    System.out.println("Total Positive comments :: "+pos);
          //  System.out.println("Total Negative comments :: "+neg);
            //System.out .println("Total comments :: "+totall);
           // System.out.println("Probability of positive comments :: "+pprob);
           // System.out.println("Probability of negative comments :: "+nprob);
            for(int nn=0;nn<array3.size();nn++)
            {
                for(int nn1=0;nn1<array1.size();nn1++)
                {
                    if(array3.get(nn).equals(array1.get(nn1)))
                    {
                        sup++;
                    }
                }
                for(int nn2=0;nn2<array2.size();nn2++)
                {
                   if(array3.get(nn).equals(array2.get(nn2)))
                   {
                       sun++;
                   }
                }
                if(sun==0&&sup==0)
                {
                     Score+=0;
                     insert(array3.get(nn),0);
                }
                else if(sup==0)
                {
                    unpprob=sup/sun;
                    //System.out.println("Unknown words positive probability :: "+unpprob);
                    unnprob=sun/sun;
                   // System.out.println("Unknown words negative probability :: "+unnprob);
                    scorep=unpprob/pprob;
                    scoren=unnprob/nprob;
                   
                    if(scorep>scoren)
                    {
                        Score+=scorep;
                        float p = (float) Math.pow(2.71,scorep);
                        scorep= (2/(1+p))-1;
                        insert(array3.get(nn),scorep);
                    }
                    else if(scorep<scoren)
                    {
                        Score=scoren;
                        float n = (float) Math.pow(2.71,scoren);
                        scoren= (2/(1+n))-1;
                        insert(array3.get(nn),scoren);
                    }
                    else
                    {
                        Score+=0;
                        insert(array3.get(nn),0);
                    }
                }
                else if(sun==0)
                {
                    unpprob=sup/sup;
                    unnprob=sun/sup;
                    scorep=unpprob/pprob;
                    scoren=unnprob/nprob;
                    if(scorep>scoren)
                    {
                        Score+=scorep;
                        float p = (float) Math.pow(2.71,scorep);
                        scorep= (2/(1+p))-1;
                        insert(array3.get(nn),scorep);
                    }
                    else if(scorep<scoren)
                    {
                        Score=scoren;
                        float n = (float) Math.pow(2.71,scoren);
                        scoren= (2/(1+n))-1;
                        insert(array3.get(nn),scoren);
                    }
                    else
                    {
                       Score+=0;
                       insert(array3.get(nn),0);
                    }
                }
                else
                {
                    unpprob=sup/sup+sun;
                 //   System.out.println("Probability that unknown word is positive ::"+unpprob);
                    unnprob=sun/sup+sun;
                  //  System.out.println("Probability that unknown word is negative ::"+unnprob);
                    scorep=unpprob/pprob;
                    scoren=unnprob/nprob;
                    if(scorep>scoren)
                    {
                       Score+=scorep;
                       float p = (float) Math.pow(2.71,scorep);
                       scorep= (2/(1+p))-1;
                       insert(array3.get(nn),scorep);
                    }
                    else if(scorep<scoren)
                    {
                       Score=scoren;
                       float n = (float) Math.pow(2.71,scoren);
                       scoren= (2/(1+n))-1;
                       insert(array3.get(nn),scoren);
                    }
                    else
                    {
                       Score+=0;
                       insert(array3.get(nn),0);
                    }
                }
            }
            return Score;
      }
    public static void insert( String a,float unknown)
    {
        try
        {
            Connection con=null;
            Class.forName("com.mysql.jdbc.Driver");     
            String url = "jdbc:mysql://localhost:3306/sentiment_analysis";  //database path 
            con = DriverManager.getConnection(url, "root", "mysql");    //connection created with username and password

            PreparedStatement pres=null;
            String query="insert into opinionwordss(opinionword,score)values(?,?)";
            pres=con.prepareStatement(query);
            pres.setString(1,a);
            pres.setFloat(2,unknown);
            pres.execute();
        }
        catch(Exception e)
        {
             //JOptionPane.showMessageDialog(null,e);
        }
    }
     public static void insert_score_in_cad1( float r_scor,float s_scor,float p_scor,float w_scor,float total_score,String query,String qu)
     {
          try
        {
            Connection con=null;
            Class.forName("com.mysql.jdbc.Driver");     
            String url = "jdbc:mysql://localhost:3306/sentiment_analysis";  //database path 
            con = DriverManager.getConnection(url, "root", "mysql");  
            PreparedStatement pres=null;//connection created with username and password
            Statement stmt = con.createStatement();
            ResultSet rss = stmt.executeQuery(qu);
            PreparedStatement upda=null;
            if (rss.next() == false) 
            {
            pres=con.prepareStatement(query);
            pres.setInt(1,1);
            pres.setFloat(2,r_scor);
            pres.setFloat(3,s_scor);
            pres.setFloat(4,p_scor);
            pres.setFloat(5,w_scor);
            pres.setFloat(6,total_score);
                        pres.execute();

 }
else
 {
     if(qu.equals(query5))
     {
         upda = con.prepareStatement( "update candidate_score SET sr_no=?,Roadlines_score=?,sanitization_score=?,publictransport_score=?,watersupply_score=?,total_score=? where sr_no='"+1+"'");
         upda.setInt(1,1); 
         upda.setFloat(2,r_scor);
            upda.setFloat(3,s_scor);
            upda.setFloat(4,p_scor);
            upda.setFloat(5,w_scor);
            upda.setFloat(6,total_score);
     }
      if(qu.equals(query6))
     {
        upda = con.prepareStatement( "update candidate2_score SET sr_no=?,Roadlines_score=?,sanitization_score=?,publictransport_score=?,watersupply_score=?,total_score=?");
        upda.setInt(1,1); 
        upda.setFloat(2,r_scor);
        upda.setFloat(3,s_scor);
        upda.setFloat(4,p_scor);
        upda.setFloat(5,w_scor);
        upda.setFloat(6,total_score);
     }
       if(qu.equals(query7))
     {
            upda = con.prepareStatement( "update candidate3_score SET sr_no=?,Roadlines_score=?,sanitization_score=?,publictransport_score=?,watersupply_score=?,total_score=?");
            upda.setInt(1,1);  
            upda.setFloat(2,r_scor);
            upda.setFloat(3,s_scor);
            upda.setFloat(4,p_scor);
            upda.setFloat(5,w_scor);
            upda.setFloat(6,total_score);
     }
     if(qu.equals(query8))
     {
        upda = con.prepareStatement( "update candidate4_score SET sr_no=?,Roadlines_score=?,sanitization_score=?,publictransport_score=?,watersupply_score=?,total_score=?");
        upda.setInt(1,1); 
        upda.setFloat(2,r_scor);
        upda.setFloat(3,s_scor);
        upda.setFloat(4,p_scor);
        upda.setFloat(5,w_scor);
        upda.setFloat(6,total_score);
     }
        upda.executeUpdate();
 
 }          

        }
        catch(Exception e)
        {
             JOptionPane.showMessageDialog(null,e);
        }
     }
    
}
