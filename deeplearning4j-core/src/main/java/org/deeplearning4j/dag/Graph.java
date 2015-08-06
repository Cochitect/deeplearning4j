package org.deeplearning4j.dag;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

/**
 * @author jeffreytang
 */
@NoArgsConstructor
@Data
public class Graph {
    // Key of adjacency list points to nodes in the list (from bottom to top)
    private Map<Node, Set<Node>> adjacencyListMap = new HashMap<>();
    private Set<Node> startNodeSet = new HashSet<>();
    private Set<Node> endNodeSet = new HashSet<>();

    public int graphSize() {
        return adjacencyListMap.size();
    }

    public void addNode(Node node) {
        if (!adjacencyListMap.containsKey(node)) {
            adjacencyListMap.put(node, new HashSet<Node>());
        }
    }

    public void addEdge(Node nodeA, Node nodeB) {
        addNode(nodeA);
        addNode(nodeB);
        adjacencyListMap.get(nodeA).add(nodeB);
    }

    public void addEdges(Node nodeA, List<Node> nodeList) {
        for (Node nodeB : nodeList) {
            addEdge(nodeA, nodeB);
        }
    }

    /**
     * Return if there is an edge of nodeA pointing towards nodeB
     *
     * @param nodeA The node pointing to another node
     * @param nodeB The node being pointed to
     * @return Boolean
     */
    public Boolean edgeBetween(Node nodeA, Node nodeB) {
        Boolean ret = false;
        if (adjacencyListMap.containsKey(nodeA)) {
            for (Node node : adjacencyListMap.get(nodeA)) {
                if (node.equals(nodeB))
                    ret = true;
            }
        }
        return ret;
    }

    /**
     * Return if the node is in the graph
     *
     * @param node The query node
     * @return Boolean
     */
    public Boolean hasNode(Node node) {
        Boolean ret = false;
        if (adjacencyListMap.containsKey(node)) {
            ret = true;
        }
        return ret;
    }

    public void addStartNode(Node startNode) {
        startNodeSet.add(startNode);
    }

    public void addEndNode(Node endNode) {
        endNodeSet.add(endNode);
    }

    /**
     * Return a list of nodes with the given name
     *
     * @param nameOfNode The query name
     * @return List of Node Objects
     */
    public List<Node> getNodesWithName(String nameOfNode) {
        List<Node> matchedNodeList = new ArrayList<>();
        for(Node node : adjacencyListMap.keySet()) {
            if (node.getName().equals(nameOfNode))
                matchedNodeList.add(node);
        }
        return matchedNodeList;
    }

    /**
     * Returns a list of nodes a particular node points to
     *
     * @param node The query node
     * @return List of neighbor Node Objects
     */
    public Set<Node> getNextNodes(Node node) {
        return adjacencyListMap.get(node);
    }

    /**
     * Turn Graph Object to customized String
     *
     * @return String representation of graph
     */
    @Override
    public String toString() {
        String sizeString = String.format("\tSize: %s\n", graphSize());
        String adjacencyString = "";
        for (Map.Entry<Node, Set<Node>> entry : adjacencyListMap.entrySet()) {
            String curNode = entry.getKey().toString();
            String listNodes = Arrays.deepToString(entry.getValue().toArray());
            adjacencyString += String.format("\t%s -> %s\n", curNode, listNodes);
        }
        String[] classList = this.getClass().toString().split("\\.");
        String className = classList[classList.length - 1];
        return String.format("%s {\n%s%s}", className, sizeString, adjacencyString);
    }
}