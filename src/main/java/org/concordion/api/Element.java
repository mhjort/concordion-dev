package org.concordion.api;

import java.util.ArrayList;
import java.util.List;

import nu.xom.Attribute;
import nu.xom.Elements;
import nu.xom.Node;
import nu.xom.Nodes;

public final class Element {

    private final nu.xom.Element xomElement;

    public Element(String name) {
        this(new nu.xom.Element(name));
    }

    /**
     * For internal use only.
     */
    public Element(nu.xom.Element xomElement) {
        this.xomElement = xomElement;
    }

    public String getText() {
        return xomElement.getValue();
    }

    public Element addStyleClass(String styleClass) {
        String currentClass = getAttributeValue("class");
        if (currentClass != null) {
            styleClass = currentClass + " " + styleClass;
        }
        addAttribute("class", styleClass);
        return this;
    }

    public Element appendNonBreakingSpace() {
        return appendText("\u00A0");
    }

    public Element appendText(String text) {
        xomElement.appendChild(new nu.xom.Text(text));
        return this;
    }

    public Element appendNonBreakingSpaceIfBlank() {
        if (isBlank()) {
            appendNonBreakingSpace();
        }
        return this;
    }

    public Element prependText(String text) {
        xomElement.insertChild(new nu.xom.Text(text), 0);
        return this;
    }

    public Element prependChild(Element element) {
        xomElement.insertChild(element.xomElement, 0);
        return this;
    }

    public void appendChild(Element element) {
        xomElement.appendChild(element.xomElement);
    }

    private Node[] getChildNodes() {
        Node[] childNodes = new Node[xomElement.getChildCount()];
        for (int i = 0; i < xomElement.getChildCount(); i++) {
            childNodes[i] = xomElement.getChild(i);
        }
        return childNodes;
    }
    
    public void moveChildrenTo(Element element) {
        for (Node childNode : getChildNodes()) {
            childNode.detach();
            element.xomElement.appendChild(childNode);
        }
    }

    public Element setId(String id) {
        addAttribute("id", id);
        return this;
    }

    public Element addAttribute(String localName, String value) {
        xomElement.addAttribute(new Attribute(localName, value));
        return this;
    }

    public String getAttributeValue(String name) {
        return xomElement.getAttributeValue(name);
    }
    
    private boolean isBlank() {
        return getText().trim().equals("");
    }

    public String toXML() {
        return xomElement.toXML();
    }

    public Element getRootElement() {
        return new Element(xomElement.getDocument().getRootElement());
    }

    public Element getFirstChildElement(String name) {
        nu.xom.Element body = xomElement.getFirstChildElement(name);
        if (body == null) {
            return null;
        }
        return new Element(body);
    }

    public boolean hasChildren() {
        return xomElement.getChildCount() > 0;
    }

    @Override
    public int hashCode() {
        return xomElement.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Element other = (Element) obj;
        if (xomElement == null) {
            if (other.xomElement != null) {
                return false;
            }
        } else if (!xomElement.equals(other.xomElement)) {
            return false;
        }
        return true;
    }

    public String getLocalName() {
        return xomElement.getLocalName();
    }
    
    public boolean isNamed(String name) {
        return getLocalName().equals(name);
    }

    public Element[] getDescendantElements(String name) {
        List<Element> descendants = new ArrayList<Element>();
        Nodes nodes = xomElement.query(".//" + name);
        for (int i = 0; i < nodes.size(); i++) {
            descendants.add(new Element((nu.xom.Element)nodes.get(i)));
        }
        descendants.remove(this);
        return descendants.toArray(new Element[0]);
    }
    
    public Element[] getChildElements(String name) {
        return wrapXomElements(xomElement.getChildElements(name));
    }

    public Element[] getChildElements() {
        return wrapXomElements(xomElement.getChildElements());
    }

    private Element[] wrapXomElements(Elements xomElements) {
        int count = xomElements.size();
        Element[] elements = new Element[count];
        for (int i = 0; i < count; i++) {
            elements[i] = new Element(xomElements.get(i));
        }
        return elements;
    }

    public Element getFirstDescendantNamed(String name) {
        Element[] descendantElements = getDescendantElements(name);
        return descendantElements.length == 0 ? null : descendantElements[0];
    }
}
