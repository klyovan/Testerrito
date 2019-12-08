package com.netcracker.testerritto.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ObjectEavBuilder {
    private BigInteger objectId;
    private BigInteger objectTypeId;
    private BigInteger parentId;
    private String name;
    private String description;
    private List<Attribute> attributes;
    private List<Reference> references;

    @Transactional
    public static class Builder {

        private JdbcTemplate jdbcTemplate;
        private ObjectEavBuilder objectEav;

        private String OBJECTS_INSERT = "insert all\n into objects(object_id, parent_id, object_type_id, name, description)\n" +
                "    values(object_id_PR.nextval, ?, ?, ?  || object_id_PR.currval, ?)\n";

        private String ATTRIBUTES_INSERT = "into attributes(object_id, attr_id, value, date_value, list_value_id)\n" +
                "    values(object_id_PR.currval, ?, ?, ?, ?)\n";

        private String REFERENCES_INSERT = "into objreference(attr_id, object_id, reference)\n" +
                "    values(?, object_id_PR.currval, ?)\n";

        private String ATTRIBUTES_VALUE_UPDATE = "update attributes set value = ?\n" +
                "    where object_id = ? and attr_id = ?;\n";

        private String ATTRIBUTES_DATE_VALE_UPDATE = "update attributes set date_value = ?\n" +
                "    where object_id = ? and attr_id = ?;\n";

        private String ATTRIBUTES_LIST_VALUE_ID_UPDATE = "update attributes set list_value_id = ?\n" +
                "    where object_id = ? and attr_id = ?;\n";

        private String OBJECTS_DELETE = "delete from objects where object_id = ?";

        private String GET_ID = "select object_id_pr.currval from dual";

        public Builder(JdbcTemplate jdbcTemplate){
            this.objectEav = new ObjectEavBuilder();
            this.objectEav.attributes = new ArrayList<>();
            this.objectEav.references = new ArrayList<>();
            this.jdbcTemplate = jdbcTemplate;
        }

        public Builder setObjectId(BigInteger objectId) {
            objectEav.objectId = objectId;
            return this;
        }

        public Builder setObjectTypeId(BigInteger objectTypeId) {
            objectEav.objectTypeId = objectTypeId;
            return this;
        }

        public Builder setParentId(BigInteger parentId) {
            objectEav.parentId = parentId;
            return this;
        }

        public Builder setName(String name) {
            objectEav.name = name;
            return this;
        }

        public Builder setDescription(String description) {
            objectEav.description = description;
            return this;
        }

        public Builder setReference(BigInteger attributeId, BigInteger referenceId) {
            Reference reference = new Reference(attributeId, referenceId);
            objectEav.references.add(reference);
            return this;
        }

        public Builder setListAttribute(BigInteger attributeId, BigInteger listValueId) {
            Attribute attribute = new Attribute(attributeId, listValueId);
            objectEav.attributes.add(attribute);
            return this;
        }

        public Builder setStringAttribute(BigInteger attributeId, String value) {
            Attribute attribute = new Attribute(attributeId, value);
            objectEav.attributes.add(attribute);
            return this;
        }

        public Builder setDateAttribute(BigInteger attributeId, Date dataValue) {
            Attribute attribute = new Attribute(attributeId, dataValue);
            objectEav.attributes.add(attribute);
            return this;
        }

        @Transactional
        public BigInteger  create(){
            String query = this.OBJECTS_INSERT;
            ArrayList<Object> objects = new ArrayList<>();
            objects.add(checkNull(this.objectEav.parentId));
            objects.add(this.objectEav.objectTypeId.toString());
            objects.add(this.objectEav.name);
            objects.add(this.objectEav.description);
            for(Attribute attribute : this.objectEav.attributes){
                query += this.ATTRIBUTES_INSERT;
                objects.add(attribute.attributeId.toString());
                objects.add(attribute.value);
                objects.add(attribute.dateValue);
                objects.add(checkNull(attribute.listValueId));
            }
            for(Reference reference : this.objectEav.references){
                query += this.REFERENCES_INSERT;
                objects.add(reference.attributeId.toString());
                objects.add(reference.referenceId.toString());
            }
            query += "select * from dual";
            this.jdbcTemplate.update(query, objects.toArray());
            return this.jdbcTemplate.queryForObject(GET_ID, BigInteger.class);
        }
        /* @Transactional
        public void update(){
             String query = "begin\n";
             ArrayList<Object> objects = new ArrayList<>();
             for(Attribute attribute : this.objectEav.attributes){
                 if(attribute.value != null){
                     query += this.ATTRIBUTES_VALUE_UPDATE;
                     objects.add(attribute.value);
                 }
                 else if(attribute.dateValue != null){
                     query += this.ATTRIBUTES_DATE_VALE_UPDATE;
                     objects.add(attribute.dateValue);
                 }
                 else {
                     query += this.ATTRIBUTES_LIST_VALUE_ID_UPDATE;
                     objects.add(attribute.listValueId.toString());
                 }
                 objects.add(this.objectEav.objectId.toString());
                 objects.add(attribute.attributeId.toString());
             }
             query += "end;\n /";
             this.jdbcTemplate.update(query, objects);
         }
 */
        @Transactional
        public void delete(){
            String query = this.OBJECTS_DELETE;
            this.jdbcTemplate.update(query, this.objectEav.objectId.toString());
        }

        private String checkNull(BigInteger id){
            if(id != null)
                return id.toString();
            else return null;
        }
    }

    private static class Attribute{

        private BigInteger attributeId;
        private String value;
        private Date dateValue;
        private BigInteger listValueId;

        private Attribute(BigInteger attributeId,  BigInteger listValueId){
            this.attributeId = attributeId;
            this.listValueId = listValueId;
        }

        private Attribute(BigInteger attributeId,  Date dateValue){
            this.attributeId = attributeId;
            this.dateValue = dateValue;
        }

        private Attribute(BigInteger attributeId,  String value){
            this.attributeId = attributeId;
            this.value = value;
        }

        private BigInteger getAttributeId() {
            return attributeId;
        }

        public String getValue() {
            return value;
        }

        public Date getDateValue() {
            return dateValue;
        }

        public BigInteger getListValueId() {
            return listValueId;
        }
    }

    private static class Reference{

        private BigInteger attributeId;
        private BigInteger referenceId;

        private Reference(BigInteger attributeId, BigInteger referenceId){
            this.attributeId = attributeId;
            this.referenceId = referenceId;
        }
    }
}
