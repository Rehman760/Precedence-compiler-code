package com.company;



import java.util.*;
import java.io.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.StringTokenizer;
import java.util.HashMap;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class NewVersion
{
    private static final char[][] precedence = {
            {'/', '1'},
            {'*', '2'},

    };

    private static int precedenceOf(String t)
    {
        char token = t.charAt(0);
        for (int i=0; i < precedence.length; i++)
        {
            if (token == precedence[i][0])
            {
                return Integer.parseInt(precedence[i][1]+"");
            }
        }
        return -1;
    }
    public static int count = 0;
    private static ArrayList<String> rules = new ArrayList<String>();
    private static ArrayList<Integer> lasttermspecialRule = new ArrayList<Integer>();
    private static ArrayList<Integer> firsttermspecialRule = new ArrayList<Integer>();
    private static ArrayList<String> parts = new ArrayList<String>();
    private static ArrayList<String> rightparts = new ArrayList<String>();
    private static ArrayList<String> terminals = new ArrayList<String>();
    private static ArrayList<Integer> indexes = new ArrayList<Integer>();
    private static ArrayList<String> inputs = new ArrayList<String>();
    private static ArrayList<String>[] parse = new ArrayList[3];
    private static ArrayList<String>[] firstterms;
    public static ArrayList<String>[] lastterms ;
    public static String [][]parseTable;
    static char nonterminal[],terminal[];
    static int nonterminalLength,terminalLength;
    static String grmr[][],fst[],flw[];

    public static void main(String args[]) throws IOException
    {

        String nt,t;
        int i,j,n;
        String scope="Local";
        nt="SE";
        nonterminalLength=nt.length();
        nonterminal=new char[nonterminalLength];
        nonterminal=nt.toCharArray();
        t="+*012345678";
        terminalLength=t.length();
        terminal=new char[terminalLength];
        terminal=t.toCharArray();

        grmr=new String[nonterminalLength][];
        grmr[0]=new String[2];
        grmr[0][0]="E-E";
        grmr[0][1]="E*E";
        grmr[1]=new String[9];
        grmr[1][0]="0";
        grmr[1][1]="1";
        grmr[1][2]="2";
        grmr[1][3]="3";
        grmr[1][4]="4";
        grmr[1][5]="5";
        grmr[1][6]="6";
        grmr[1][7]="7";
        grmr[1][8]="8";

        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("|                                                                                  |");
        System.out.println("|   WELCOME TO OPERATOR PRECEDENCE PARSER And INTERMEDIATION CODE REPRESENTATION   |");
        System.out.println("|                                                                                  |");
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("---------------------------------------------");
        System.out.println("|                                           |");
        System.out.println("|           Grammar of our Parser           |");
        System.out.println("|                                           |");
        System.out.println("---------------------------------------------");
        System.out.println("|                                           |");
        System.out.println("|                S-----> E*E                |");
        System.out.println("|                S-----> E/E                |");
        System.out.println("|                E-----> 0                  |");
        System.out.println("|                E-----> 1                  |");
        System.out.println("|                E-----> 2                  |");
        System.out.println("|                E-----> 3                  |");
        System.out.println("|                E-----> 4                  |");
        System.out.println("|                E-----> 5                  |");
        System.out.println("|                E-----> 6                  |");
        System.out.println("|                E-----> 7                  |");
        System.out.println("|                E-----> 8                  |");
        System.out.println("|                E-----> 9                  |");
        System.out.println("---------------------------------------------");
        System.out.println();
        System.out.println();
        System.out.println();


        fst=new String[nonterminalLength];
        for(i=0;i<nonterminalLength;i++)
            fst[i]=first(i);
        flw=new String[nonterminalLength];
        for(i=0;i<nonterminalLength;i++)
            flw[i]=follow(i);

        //Here Parser code start
        System.out.print("Enter any Expression(including numbers and operators(*,/)): ");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        int sizeOfInput = input.length();

        //Checking is : in the end or not
        if(!(input.charAt(sizeOfInput-1)=='$'))
        {
            System.out.println("SyntaxError!!!!");
            System.out.println("Need of '$' in the last!!!");
            return;
        }

        /////////Converting and adding space to the input
        char[] ch = new char[input.length()];
        String tempToken="";
        for(int s=0; s<input.length(); s++)
        {
            ch[s] = input.charAt(s);
            tempToken+=ch[s]+" ";
        }

        ///////////////////////////Now for Tokenization and SymbolTable//////////////
        String temperary;
        String inputToken=tempToken;
        StringTokenizer st = new StringTokenizer(inputToken," ");
        HashMap<String, String> symtab = new HashMap<String, String>();

        String Lexeme = "";
        String Token = "Literals, Operators, Special Character";
        String printToken = "";
        i=1;
        while (st.hasMoreTokens())
        {
            temperary=st.nextToken();
            Lexeme += temperary;
            Lexeme += " , ";
            if(temperary.equals("*") ||temperary.equals("/") ||temperary.equals("-") ||temperary.equals("+") )
            {
                printToken += "<";
                printToken += temperary;
                printToken += "> ";
                symtab.put(temperary+" "," < "+temperary+" >");
                continue;
            }
            else if(temperary.equals("$"))
            {
                printToken += "<";
                printToken += temperary;
                printToken += "> ";
                symtab.put(temperary+" "," < "+temperary+" >");
                continue;
            }
            else if(temperary.charAt(0)>=48 && temperary.charAt(0)<=57)
            {
                printToken += "<num,";
                printToken += temperary;
                printToken += "> ";
                symtab.put(temperary+" "," < "+temperary+" >");
                continue;
            }
            else
            {
                System.out.println(temperary+" is invalid token.");
                return;
            }
        }

        //store tokens in file
        PrintStream ps = new PrintStream(new File("token.txt"));
        ps.println("     Input: "+inputToken+"     ");
        ps.println("     Tokens: "+Token);
        ps.println("     Lexemes: "+Lexeme);
        ps.println("     Above Lexemes are mapped into following tokens: "+printToken);
        ps.close();
        //////////////


        try{
            FileWriter mywrite = new FileWriter("WriteInput.txt");
            mywrite.write(""+symtab);
            mywrite.close();
            //System.out.println("Wrote SUCCESSFULLY");

        }catch(IOException e)
        {
            System.out.println("Error during writing in file");
        }



        /////////////////////////////Symbol Table///////////////////////////////////////////
        String type,nameofidentifier;
        if(input instanceof String)
        {
            type = "String";
        }
        else
        {
            type = "Not String";
        }

        nameofidentifier = requestIdentifierName(input);

        System.out.println();
        System.out.println();
        System.out.println();
//        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
//        System.out.println("+                                           +");
//        System.out.println("+                Symbol Table               +");
//        System.out.println("+                                           +");
//        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
//        System.out.println("+                                           +");
//        System.out.println("+        Symbol  --   Type  --  Scope       +");
//        System.out.println("+                                           +");
//        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
//        System.out.println();
//        System.out.println("        "+nameofidentifier+"       "+type+"       "+scope);
//        System.out.println();
//        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
//        System.out.println();
//        System.out.println();
//        System.out.println();
//        ////////////////////////Symbol Table is Above///////////////////////

        rules.add("S ::- $E$;"); //The grammar
        rules.add("E ::- E*T|T;");
        rules.add("T ::- T/F|F;");
        rules.add("F ::- (E)|0|1|2|3|4|5|6|7|8|9;");
        n = rules.size();
        StringBuilder sb= new StringBuilder();
        for(i=0; i<n; i++)
            sb.append(rules.get(i));
        String grammar = sb.toString();
        for(i=0; i<grammar.length(); i++)
        {
            if(lowercase(grammar.charAt(i)) && nonterminal(grammar.charAt(i)))
                terminals.add(Character.toString(grammar.charAt(i)));
        }
        Set<String> set = new HashSet<>(terminals);
        terminals.clear();
        terminals.addAll(set);
        parseTable = new String[terminals.size()+1][terminals.size()+1];

        for(i=0; i<n; i++)
        {
            rightparts.addAll(partFinder(rules.get(i)));
        }
        int size = rightparts.size();
        for(i=0; i<size; i++)
        {
            String str = rightparts.get(i);
            char []strchar = str.toCharArray();
            for(j=0; j<str.length(); j++)
            {
                if(uppercase(str.charAt(j)))
                {
                    strchar[j]='N';
                }
            }
            str = String.copyValueOf(strchar);
            rightparts.set(i, str);//creating the elements

        }
        set = new HashSet<>(rightparts);//eliminating repetitive elements
        rightparts.clear();
        rightparts.addAll(set);

        for(i=0; i<terminals.size()+1; i++)
        { //initialization
            for(j=0; j<terminals.size()+1; j++)
            {
                parseTable[i][j]= "e";
            }
        }

        int k=1;
        for(i=0; i<terminals.size();i++)
        {
            parseTable[k][0] = terminals.get(i);
            parseTable[0][k] = terminals.get(i);
            k++;
        }
        parseTable[0][0]=" ";

        lastterms = new ArrayList[n];//keeping lastterms for each variable
        firstterms = new ArrayList[n];//keeping firstterms for each variable
        //############ initializing
        for (i = 0; i < n; i++)
        {
            lastterms[i] = new ArrayList<String>();
        }
        for (i = 0; i < n; i++)
        {
            firstterms[i] = new ArrayList<String>();
        }
        for (i = 0; i < 3; i++)
        {
            parse[i] = new ArrayList<String>();
        }


        //########################### Ending initialization

        count =0; //clear the global variable for lastterms
        for (i = 0; i < n; i++)
        {
            //call the function for each rule
            lasttermFinder(rules.get(i));
        }

        for (i = 0; i < n; i++) //call the function for the number of variables for assurance
            completeLastterm(lasttermspecialRule);


        count =0;//clear the global variable for firstterms
        for ( i = 0; i < n; i++)
        {
            //call the function for each rule
            firsttermFinder(rules.get(i));
        }


        for ( i = 0; i < n; i++) //call the function for the number of variables for assurance
            completeFirstterm(firsttermspecialRule);

        parseTableGenerator(rules);//filling parse table

        parse[0].add("$");
        parse[1].add(input);
        parser(input);




        //store parse table
        PrintStream printStream = new PrintStream(new File("parsetable.csv"));
        printStream.println("Stack,inputs,Action");
        for ( i = 0; i <parse[0].size()-1; i++)
        {
            //printing parse
            printStream.println(parse[0].get(i)+","+ parse[1].get(i)+","+ parse[2].get(i));
        }
        printStream.close();


        //System.out.println(input.substring(0,input.length()-1));
        intermediation(input);
    }

    static void intermediation(String str){
        String e=str.substring(0,str.length()-1);
        int i, j, opc=0;
        char token;
        boolean processed[];
        String[][] operators = new String[10][2];
        String expr="", temp;

        //BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        //System.out.print("\nEnter an expression: ");
        //expr = in.readLine();
        expr=str;
        processed = new boolean[expr.length()];
        for (i=0; i < processed.length; i++)
        {
            processed[i] = false;
        }
        for (i=0; i < expr.length(); i++) {
            token = expr.charAt(i);
            for (j = 0; j < precedence.length; j++) {
                if (token == precedence[j][0]) {
                    operators[opc][0] = token + "";
                    operators[opc][1] = i + "";
                    opc++;
                    break;
                }
            }
        }
        //sort
        for (i=opc-1; i >= 0; i--)
        {
            for (j=0; j < i; j++)
            {
                if (precedenceOf(operators[j][0]) > precedenceOf(operators[j+1][0]))
                {
                    temp = operators[j][0];
                    operators[j][0] = operators[j+1][0];
                    operators[j+1][0] = temp;
                    temp = operators[j][1];
                    operators[j][1] = operators[j+1][1];
                    operators[j+1][1] = temp;
                }
            }
        }
        System.out.println("\nOperators sorted in their precedence:\nOperator\tLocation");
        for (i=0; i < opc; i++)
        {
            System.out.println(operators[i][0] + "\t\t\t" + operators[i][1]);
        }
        System.out.println();
        for (i=0; i < opc; i++)
        {
            j = Integer.parseInt(operators[i][1]+"");
            String op1="", op2="";
            if (processed[j-1]==true)
            {
                if (precedenceOf(operators[i-1][0]) == precedenceOf(operators[i][0]))
                {
                    op1 = "t"+i;
                }
                else
                {
                    for (int x=0; x < opc; x++)
                    {
                        if ((j-2) == Integer.parseInt(operators[x][1]))
                        {
                            op1 = "t"+(x+1)+"";
                        }
                    }
                }
            }
            else
            {
                op1 = expr.charAt(j-1)+"";
            }
            if (processed[j+1]==true)
            {
                for (int x=0; x < opc; x++)
                {
                    if ((j+2) == Integer.parseInt(operators[x][1]))
                    {
                        op2 = "t"+(x+1)+"";
                    }
                }
            }
            else
            {
                op2 = expr.charAt(j+1)+"";
            }
            System.out.println("t"+(i+1)+" = "+op1+operators[i][0]+op2);
            processed[j] = processed[j-1] = processed[j+1] = true;
        }

    }

    static String requestIdentifierName(String input)
    {

        return "input";
    }



    static String first(int i)
    {
        int j,k,l=0,found=0;
        String temp="",str="";
        for(j=0;j<grmr[i].length;j++) //number of productions << length=2
        {
            for(k=0;k<grmr[i][j].length();k++,found=0) //grmr[0][0]=E+E, length=3
            {
                for(l=0;l<nonterminalLength;l++) //finding nonterminal, length=2
                {
                    if(grmr[i][j].charAt(k)==nonterminal[l]) // nontermil="SE"
                    {
                        str=first(l);
                        if(!(str.length()==1))
                            temp=temp+str;
                        found=1;
                        break;
                    }
                }
                if(found==1)
                {
                    if(str.contains("9")) //here epsilon will lead to next nonterminal’s first set
                        continue;
                }
                else //if first set includes terminal
                    temp=temp+grmr[i][j].charAt(k);
                break;
            }
        }
        return temp;
    }

    static String follow(int i)
    {
        char pro[],chr[];
        String temp="";
        int j,k,l,m,n,found=0;
        if(i==0)
            temp="$";
        for(j=0;j<nonterminalLength;j++)
        {
            for(k=0;k<grmr[j].length;k++) //entering grammar matrix
            {
                pro=new char[grmr[j][k].length()];//S -->   E*E || E/E
                pro=grmr[j][k].toCharArray();
                for(l=0;l<pro.length;l++) //entering each production
                {
                    if(pro[l]==nonterminal[i])  //finding the nonterminal whose follow set is to be found
                    {
                        if(l==pro.length-1) //if it is the last terminal/non-terminal then follow of current non-terminal
                        {
                            if(j<i)
                                temp=temp+flw[j];
                        }
                        else
                        {
                            for(m=0;m<nonterminalLength;m++)
                            {
                                if(pro[l+1]==nonterminal[m]) //first of next non-terminal otherwise (else later…)
                                {
                                    chr=new char[fst[m].length()];
                                    chr=fst[m].toCharArray();
                                    for(n=0;n<chr.length;n++)
                                    {
                                        if(chr[n]=='9') //if first includes epsilon
                                        {
                                            if(l+1==pro.length-1)
                                                temp=temp+follow(j); //when non-terminal is second last
                                            else
                                                temp=temp+follow(m);
                                        }
                                        else
                                            temp=temp+chr[n]; //include whole first set except epsilon
                                    }
                                    found=1;
                                }
                            }
                            if(found!=1)
                                temp=temp+pro[l+1]; //follow set will include terminal(else is here)
                        }
                    }
                }
            }
        }
        return temp;
    }

    static String removeDuplicates(String str)
    {
        int i;
        char ch;
        boolean seen[] = new boolean[256];
        StringBuilder sb = new StringBuilder(seen.length);
        for(i=0;i<str.length();i++)
        {
            ch=str.charAt(i);
            if (!seen[ch])
            {
                seen[ch] = true;
                sb.append(ch+",");
            }
        }
        return "{"+sb.toString()+"}";
    }

    public static int ruleFinder(char a)
    {
        for(int i=0; i<rules.size(); i++)
        {
            if(rules.get(i).charAt(0)==a)
                return i;
        }
        return 0;
    }

    //This function will insert an item in the parse table
    public static void insertParseTable(String s1, String s2, String str)
    {
        int row = 0 ,column = 0;
        for(int i=0; i<terminals.size()+1; i++)
        {
            if(parseTable[i][0].equals(s1)) row=i;
            if(parseTable[0][i].equals(s2)) column=i;
        }
        parseTable[row][column] = str;
    }

    //This function will help eliminating nonterminals
    public static boolean nonterminal(char a)
    {
        if(a==';' || a==':' ||a=='-' ||a=='|' ||a==' ') return false;
        return true;
    }

    //This function will recognize a lowercase which is a terminal
    public static boolean lowercase(char a)
    {
        if(a<65 || a>90) return true;
        else return false;
    }

    //This function will recognize an uppercase which is a variable
    public static boolean uppercase(char a)
    {
        if(a>=65 && a<=90) return true;
        else return false;
    }

    //This function will divide the right side of the given rules
    public static ArrayList<String> partFinder(String rule)
    {
        parts.clear();
        int pos = 0;
        for(int i=0; i<rule.length(); i++)
        {
            if(rule.charAt(i)=='-' || rule.charAt(i)=='|' ||rule.charAt(i)==';')
            {
                parts.add(rule.substring(pos+1,i).trim());
                pos = i;
            }
        }
        parts.remove(0);//first element is not needed
        return parts;
    }

    //This function will find the initial firstterms
    public static void firsttermFinder(String rule)
    {
        partFinder(rule);
        for(int i=0; i<parts.size();i++){
            if(parts.get(i).length()==1){
                if(lowercase(parts.get(i).charAt(0)))
                    firstterms[count].add(parts.get(i));
                else if(uppercase(parts.get(i).charAt(0)))
                    firsttermspecialRule.add(count);//we deal with this later
            }
            else{
                String str = parts.get(i);
                if(lowercase(str.charAt(0))) {
                    firstterms[count].add(Character.toString(str.charAt(0)));
                } else if(uppercase(str.charAt(0))){
                    if(lowercase(str.charAt(1))) {
                        firstterms[count].add(Character.toString(str.charAt(1)));
                    }
                }

            }
        }
        count ++;
    }

    //This function will complete the firstterms
    public static void completeFirstterm(ArrayList specialRule)
    {
        //needs modification
        for(int i=0; i<specialRule.size();i++){
            int num = (int)specialRule.get(i);
            ArrayList <String>parts = partFinder(rules.get((int)specialRule.get(i)));
            for (int j=0; j<parts.size();j++){
                if(parts.get(j).length()==1 && uppercase(parts.get(j).charAt(0))){
                    char key = parts.get(j).charAt(0);//the letter of the special rule
                    for (int k=0; k<rules.size();k++){//traverse over the rules for match
                        if(rules.get(k).charAt(0)==key){//assumption: no space at the beginning of the rule
                            /*k is the rule which its firstterms must be added
                             * to the current variable (num)*/
                            for(int m=0; m<firstterms[k].size(); m++){
                                if(!firstterms[num].contains(firstterms[k].get(m)))//if it is not already in the list
                                    firstterms[num].add(firstterms[k].get(m));
                            }

                        }
                    }
                }
            }
        }
    }

    //This function will find the initial lastterms
    public static void lasttermFinder(String rule)
    {

        partFinder(rule);
        for(int i=0; i<parts.size();i++){
            if(parts.get(i).length()==1){
                if(lowercase(parts.get(i).charAt(0)))
                    lastterms[count].add(parts.get(i));
                else if(uppercase(parts.get(i).charAt(0)))
                    lasttermspecialRule.add(count);//we deal with this later
            }
            else{
                String str = parts.get(i);
                if(lowercase(str.charAt(str.length()-1))) {
                    lastterms[count].add(Character.toString(str.charAt(str.length()-1)));
                } else if(uppercase(str.charAt(str.length()-1))){
                    if(lowercase(str.charAt(str.length()-2))) {
                        lastterms[count].add(Character.toString(str.charAt(str.length()-2)));
                    }
                }

            }
        }
        count ++;
    }

    //This function will complete the lastterms
    public static void completeLastterm(ArrayList specialRule)
    {//needs modification
        for(int i=0; i<specialRule.size();i++){
            int num = (int)specialRule.get(i);
            ArrayList <String>parts = partFinder(rules.get((int)specialRule.get(i)));
            for (int j=0; j<parts.size();j++){
                if(parts.get(j).length()==1 && uppercase(parts.get(j).charAt(0))){
                    char key = parts.get(j).charAt(0);//the letter of the special rule
                    for (int k=0; k<rules.size();k++){//traverse over the rules for match
                        if(rules.get(k).charAt(0)==key){//assumption: no space at the beginning of the rule
                            /*k is the rule which its lastterms must be added
                             * to the current variable (num)*/
                            for(int m=0; m<lastterms[k].size(); m++){
                                if(!lastterms[num].contains(lastterms[k].get(m)))//if it is not already in the list
                                    lastterms[num].add(lastterms[k].get(m));
                            }

                        }
                    }
                }
            }
        }
    }

    //This function will fill the parse table with appropriate contents
    public static void parseTableGenerator(ArrayList<String> grammar)
    {
        for(int i=0; i<grammar.size(); i++){
            ArrayList <String>parts = partFinder(grammar.get(i));
            for(int j=0; j<parts.size(); j++){
                String part = parts.get(j);
                if(part.length()>1){
                    for(int k=0; k<part.length()-1; k++){
                        String substr = part.substring(k, k+2);
                        if(lowercase(substr.charAt(0)) && lowercase(substr.charAt(1))){
                            insertParseTable(Character.toString(substr.charAt(0)), Character.toString(substr.charAt(1)), "=");
                        }else if(lowercase(substr.charAt(0)) && uppercase(substr.charAt(1))){
                            int var = ruleFinder(substr.charAt(1));
                            for(int m=0; m<firstterms[var].size(); m++){
                                insertParseTable(Character.toString(substr.charAt(0)), firstterms[var].get(m),"<");
                            }
                        }
                        else if(uppercase(substr.charAt(0)) && lowercase(substr.charAt(1))){
                            int var = ruleFinder(substr.charAt(0));
                            for(int m=0; m<lastterms[var].size(); m++){
                                insertParseTable(lastterms[var].get(m), Character.toString(substr.charAt(1)), ">");
                            }
                        }
                        if(part.length() >= 3){
                            for(int n=0; n<part.length()-2; n++){
                                String sub = part.substring(n,n+3);
                                if(lowercase(sub.charAt(0)) && uppercase(sub.charAt(1)) && lowercase(sub.charAt(2))){
                                    insertParseTable(Character.toString(sub.charAt(0)),Character.toString(sub.charAt(2)), "=");
                                }
                            }
                        }

                    }
                }
            }
        }
    }

    //This function will show the proper comment
    public static void SyntaxError()
    {
        System.out.println("The parser encountered a problem while parsing the input string.");
    }

    //This function will show the acceptance of the string
    public static void success()
    {
        System.out.println("String was parsed successfully!");
    }

    //This function will get the result from the parse table
    public static String fetch(char a, char b)
    {
        int row = 0 ,column = 0;
        String s1 = Character.toString(a);
        String s2 = Character.toString(b);
        for(int i=0; i<terminals.size()+1; i++){
            if(parseTable[i][0].equals(s1)) row=i;
            if(parseTable[0][i].equals(s2)) column=i;
        }
        return parseTable[row][column];
    }

    //This function will correct the handle
    public static String correct(String handle)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(handle);
        sb.deleteCharAt(handle.indexOf('<'));
        String str=sb.toString();
        return str;
    }

    //This function will parse the input string
    public static void parser(String input)
    {
        StringBuilder sb = new StringBuilder();
        String handle = null;
        for(int i=0; i<200; i++)
        {
            if(parse[0].get(i).equals("$N") && parse[1].get(i).equals("$"))
            {
                parse[0].add("$N");
                parse[1].add("$");
                parse[2].add("accept");
                success();
                return;
            }
            else
            {
                String yardstick = null;
                if(parse[0].get(i).charAt(parse[0].get(i).length()-1)!='N')
                    yardstick = fetch(parse[0].get(i).charAt(parse[0].get(i).length()-1),parse[1].get(i).charAt(0));//passing arguments
                if(parse[0].get(i).charAt(parse[0].get(i).length()-1)=='N')
                    yardstick = fetch(parse[0].get(i).charAt(parse[0].get(i).length()-2),parse[1].get(i).charAt(0));
                if(yardstick.equals("e"))
                {
                    //the symbol table entry is empty
                    SyntaxError();
                    return;
                }
                else if(yardstick.equals("="))
                {
                    sb = new StringBuilder();
                    sb.append(parse[0].get(i));
                    sb.append(parse[1].get(i).charAt(0));
                    String str = sb.toString();
                    parse[0].add(str);
                    sb = new StringBuilder();
                    str = parse[1].get(i);
                    sb.append(str);
                    sb.deleteCharAt(0);
                    str = sb.toString();
                    parse[1].add(str);
                    parse[2].add("shift");
                }
                else if(yardstick.equals("<"))
                {
                    sb = new StringBuilder();
                    sb.append(parse[0].get(i));
                    sb.append("<");
                    sb.append(parse[1].get(i).charAt(0));
                    String str = sb.toString();
                    parse[0].add(str);
                    sb = new StringBuilder();
                    str = parse[1].get(i);
                    sb.append(str);
                    sb.deleteCharAt(0);
                    str = sb.toString();
                    parse[1].add(str);
                    parse[2].add("shift");
                }
                else if(yardstick.equals(">"))
                {
                    sb = new StringBuilder();
                    int index =0;
                    String str= parse[0].get(i);
                    for(int j = str.length()-1; j>=0; j--)
                    {
                        if(str.charAt(j) == '<')
                        {
                            index=j;
                            break;
                        }

                    }
                    if(str.charAt(index-1)!='N')
                    {
                        handle = str.substring(index,str.length());
                    }
                    else
                    {
                        handle = str.substring(index-1,str.length());
                    }
                    String result= correct(handle);
                    if(!rightparts.contains(result))
                    {
                        //if the handle does not match with the right side of any rules
                        parse[0].add(parse[0].get(i));
                        parse[1].add(parse[1].get(i));
                        parse[2].add("syntax error");
                        SyntaxError();
                        return;
                    }
                    if(str.charAt(index-1) == 'N')
                        index--;
                    sb.append(str);
                    sb.delete(index, str.length());
                    sb.append("N");
                    str = sb.toString();
                    parse[0].add(str);
                    parse[1].add(parse[1].get(i));
                    parse[2].add("reduce");

                }
            }
        }
    }
}

