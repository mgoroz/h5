import java.util.*;

public class Node {

   private String name;
   private Node firstChild;
   private Node nextSibling;

   Node(String n, Node d, Node r) {
      if (n == null || n.isEmpty() || n.matches(".*\\s.*")) {
         throw new RuntimeException("Invalid node name: " + n);
      }
      this.name = n;
      this.firstChild = d;
      this.nextSibling = r;
   }


   public static Node parsePostfix(String s) {
      if (s == null || s.isEmpty()) {
         throw new RuntimeException("Invalid input string: " + s);
      }

      Stack<Node> stack = new Stack<>();
      int i = 0;

      while (i < s.length()) {
         char c = s.charAt(i);

         if (c != '(' && c != ')' && c != ',' && !Character.isWhitespace(c)) {
            StringBuilder nodeName = new StringBuilder();
            while (i < s.length() && s.charAt(i) != '(' && s.charAt(i) != ')' && s.charAt(i) != ',' && !Character.isWhitespace(s.charAt(i))) {
               nodeName.append(s.charAt(i));
               i++;
            }

            Node parent = new Node(nodeName.toString(), null, null);

            if (!stack.isEmpty() && stack.peek().name.equals(")")) {
               // there are children in stack

               stack.pop(); // pop ")"

               List<Node> children = new ArrayList<>();
               while (!stack.isEmpty() && !stack.peek().name.equals("(")) {
                  Node child = stack.pop();
                  children.add(0, child);

                  if (!stack.isEmpty() && !stack.peek().name.equals("(")){
                     if (!stack.peek().name.equals(",")) {
                        // there was no "," between children
                        throw new RuntimeException("Invalid input string: " + s);
                     }
                     stack.pop(); // pop ","
                     if (!stack.isEmpty() && stack.peek().name.equals("(")){
                        // expecting a sibling before ","
                        throw new RuntimeException("Invalid input string: " + s);
                     }
                  }
               }

               if (stack.isEmpty()) {
                  // there was no "("
                  throw new RuntimeException("Invalid input string: " + s);
               }
               stack.pop(); // pop "("

               parent.firstChild = children.get(0);
               for (int j = 0; j < children.size() - 1; j++) {
                  children.get(j).nextSibling = children.get(j + 1);
               }
            }

            stack.push(parent);
         } else if (c == '(') {
            stack.push(new Node("(", null, null));
            i++;
         } else if (c == ',') {
            stack.push(new Node(",", null, null));
            i++;
         } else if (c == ')') {
            stack.push(new Node(")", null, null));
            i++;
         } else {
            throw new RuntimeException("Invalid character " + c + " in input string: " + s);
         }
      }

      if (stack.size() != 1) {
         throw new RuntimeException("Invalid input string2: " + s);
      }

      return stack.pop();
   }




   public String leftParentheticRepresentation() {
      StringBuilder result = new StringBuilder();
      result.append(this.name);

      if (this.firstChild != null) {
         result.append("(");
         result.append(this.firstChild.leftParentheticRepresentation());
         result.append(")");
      }

      if (this.nextSibling != null) {
         result.append(",");
         result.append(this.nextSibling.leftParentheticRepresentation());
      }

      return result.toString();
   }

   public static void main (String[] param) {
      String s = "(B1,C)A";
      Node t = Node.parsePostfix (s);
      String v = t.leftParentheticRepresentation();
      System.out.println (s + " ==> " + v); // (B1,C)A ==> A(B1,C)
   }
}