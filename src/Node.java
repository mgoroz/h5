import java.util.*;

public class Node {

   private String name;
   private Node firstChild;
   private Node nextSibling;

   Node(String n, Node d, Node r) {
      if (n == null) { // bypass validation for sentinel nodes
         this.name = n;
         this.firstChild = d;
         this.nextSibling = r;
         return;
      }

      if (n.isEmpty() || n.contains("(") || n.contains(")") || n.contains(",") || n.contains(" ")) {
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

         if (Character.isLetterOrDigit(c)) {
            StringBuilder nodeName = new StringBuilder();
            while (i < s.length() && Character.isLetterOrDigit(s.charAt(i))) {
               nodeName.append(s.charAt(i));
               i++;
            }
            stack.push(new Node(nodeName.toString(), null, null));
         } else if (c == '(') {
            i++;
         } else if (c == ',') {
            i++;
         } else if (c == ')') {
            List<Node> children = new ArrayList<>();
            while (!stack.isEmpty() && stack.peek().nextSibling == null && stack.peek().firstChild == null) {
               Node child = stack.pop();
               children.add(0, child);
            }

            if (!stack.isEmpty()) {
               Node parent = stack.pop();
               parent.firstChild = children.get(0);
               for (int j = 0; j < children.size() - 1; j++) {
                  children.get(j).nextSibling = children.get(j + 1);
               }
               stack.push(parent);
            } else {
               throw new RuntimeException("Invalid input string: " + s);
            }

            i++;
         } else {
            throw new RuntimeException("Invalid character in input string: " + c);
         }
      }

      if (stack.size() != 1) {
         throw new RuntimeException("Invalid input string: " + s);
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