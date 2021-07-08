package com.unilog.misc;

import java.util.ArrayList;

public class EventModel {

	private String updatedDatetime; 
	private String paymentOptionSelected;           
	private String contactMethod; 
	private String isCostRequired;                       
	private String isFaxRequired;                           
	private int isAllDayEvent;                               
	private String color;                                   
	private String eventBodyKeyword;                    
	private int recurringRule;                                 
	private String timeZone;                         
	private String featuredEvents;  
	private String status;                                   
	private String displayOnline;                            
	private int customerNotificationId;                 
	private int internalNotificationId;                       
	private int clientCost;                                   
	private String timezoneOffset;  
	private String title;
	private String description;
	private int id;
	private String date;
	private String end;
	private Double cost;
	private String address;
	private String location;
	private String eventCategory;
	private String contact;
	private int totalSeats;
	private int bookedSeats;
	private String eventType;
	private String fileLocation;
	private String notification;
	private String showSeats;
	private String blockOnlineReg;

	
	private int	eventRegId ;        
	private String firstName;                      
	private int eventId;                    
	private String lastName; 
	private String email;                            
	private String phoneNumber;                   
	private String eventSubscription;        
	private String paymentMode;                    
	private String eventSubscriptionNote;       
	private String companyName;                       
	private String jobTitle;                         
	private String department;                        
	private String faxNumber;                    
	private int userId;                             
	private String 	registrationStatus;       
	private int parentId;                      
	private String address2;                      
	private String city;                    
	private String state;                      
	private String country;               
	private String salesPerson;             
	private String serviceCenter;                
	private int salesPrice;                          
	private int amountPaid;                       
	private String accountNumber;                 
	private String registeredDate;                     
	private int userEdited;                                  
	private String poNumber;
	private String mailSentStatus;
    private int availableSeats;

    private String[] eventLocationFilter;
    private String[] eventCategoryFilter;
    
    private String durationDays;
    private String durationHours;
    private String durationMinutes;
    private String dateV2;
    private String endV2;
    private int startDay;
    private int endDay;
    private String month;
    private String fieldValue;
    private String fieldName;
    private EventModel eventsCustomFieldList = null;
    private String imageName;
    ArrayList<EventModel> customFieldsList = new ArrayList<EventModel>();
    private String additionalInformation;     
    
    
	public ArrayList<EventModel> getCustomFieldsList() {
		return customFieldsList;
	}
	public void setCustomFieldsList(ArrayList<EventModel> customFieldsList) {
		this.customFieldsList = customFieldsList;
	}
	public int getStartDay() {
		return startDay;
	}
	public void setStartDay(int startDay) {
		this.startDay = startDay;
	}
	public int getEndDay() {
		return endDay;
	}
	public void setEndDay(int endDay) {
		this.endDay = endDay;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getDateV2() {
		return dateV2;
	}
	public void setDateV2(String dateV2) {
		this.dateV2 = dateV2;
	}
	public String getEndV2() {
		return endV2;
	}
	public void setEndV2(String endV2) {
		this.endV2 = endV2;
	}
	public String getDurationDays() {
		return durationDays;
	}
	public void setDurationDays(String durationDays) {
		this.durationDays = durationDays;
	}
	public String getDurationHours() {
		return durationHours;
	}
	public void setDurationHours(String durationHours) {
		this.durationHours = durationHours;
	}
	public String getDurationMinutes() {
		return durationMinutes;
	}
	public void setDurationMinutes(String durationMinutes) {
		this.durationMinutes = durationMinutes;
	}
	public int getAvailableSeats() {
		return availableSeats;
	}
	public void setAvailableSeats(int availableSeats) {
		this.availableSeats = availableSeats;
	}
	public String getUpdatedDatetime() {
		return updatedDatetime;
	}
	public void setUpdatedDatetime(String updatedDatetime) {
		this.updatedDatetime = updatedDatetime;
	}
	public String getPaymentOptionSelected() {
		return paymentOptionSelected;
	}
	public void setPaymentOptionSelected(String paymentOptionSelected) {
		this.paymentOptionSelected = paymentOptionSelected;
	}
	public String getContactMethod() {
		return contactMethod;
	}
	public void setContactMethod(String contactMethod) {
		this.contactMethod = contactMethod;
	}

	public String getIsCostRequired() {
		return isCostRequired;
	}
	public void setIsCostRequired(String isCostRequired) {
		this.isCostRequired = isCostRequired;
	}
	public String getIsFaxRequired() {
		return isFaxRequired;
	}
	public void setIsFaxRequired(String isFaxRequired) {
		this.isFaxRequired = isFaxRequired;
	}
	public int getIsAllDayEvent() {
		return isAllDayEvent;
	}
	public void setIsAllDayEvent(int isAllDayEvent) {
		this.isAllDayEvent = isAllDayEvent;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getEventBodyKeyword() {
		return eventBodyKeyword;
	}
	public void setEventBodyKeyword(String eventBodyKeyword) {
		this.eventBodyKeyword = eventBodyKeyword;
	}
	public int getRecurringRule() {
		return recurringRule;
	}
	public void setRecurringRule(int recurringRule) {
		this.recurringRule = recurringRule;
	}
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	public String getFeaturedEvents() {
		return featuredEvents;
	}
	public void setFeaturedEvents(String featuredEvents) {
		this.featuredEvents = featuredEvents;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDisplayOnline() {
		return displayOnline;
	}
	public void setDisplayOnline(String displayOnline) {
		this.displayOnline = displayOnline;
	}
	public int getCustomerNotificationId() {
		return customerNotificationId;
	}
	public void setCustomerNotificationId(int customerNotificationId) {
		this.customerNotificationId = customerNotificationId;
	}
	public int getInternalNotificationId() {
		return internalNotificationId;
	}
	public void setInternalNotificationId(int internalNotificationId) {
		this.internalNotificationId = internalNotificationId;
	}
	public int getClientCost() {
		return clientCost;
	}
	public void setClientCost(int clientCost) {
		this.clientCost = clientCost;
	}
	
	public String getTimezoneOffset() {
		return timezoneOffset;
	}
	public void setTimezoneOffset(String timezoneOffset) {
		this.timezoneOffset = timezoneOffset;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	public Double getCost() {
		return cost;
	}
	public void setCost(Double cost) {
		this.cost = cost;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getEventCategory() {
		return eventCategory;
	}
	public void setEventCategory(String eventCategory) {
		this.eventCategory = eventCategory;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public int getTotalSeats() {
		return totalSeats;
	}
	public void setTotalSeats(int totalSeats) {
		this.totalSeats = totalSeats;
	}
	public int getBookedSeats() {
		return bookedSeats;
	}
	public void setBookedSeats(int bookedSeats) {
		this.bookedSeats = bookedSeats;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getFileLocation() {
		return fileLocation;
	}
	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}
	public String getNotification() {
		return notification;
	}
	public void setNotification(String notification) {
		this.notification = notification;
	}
	public String getShowSeats() {
		return showSeats;
	}
	public void setShowSeats(String showSeats) {
		this.showSeats = showSeats;
	}
	public String getBlockOnlineReg() {
		return blockOnlineReg;
	}
	public void setBlockOnlineReg(String blockOnlineReg) {
		this.blockOnlineReg = blockOnlineReg;
	}
	public int getEventRegId() {
		return eventRegId;
	}
	public void setEventRegId(int eventRegId) {
		this.eventRegId = eventRegId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public int getEventId() {
		return eventId;
	}
	public void setEventId(int eventId) {
		this.eventId = eventId;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getEventSubscription() {
		return eventSubscription;
	}
	public void setEventSubscription(String eventSubscription) {
		this.eventSubscription = eventSubscription;
	}
	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	public String getEventSubscriptionNote() {
		return eventSubscriptionNote;
	}
	public void setEventSubscriptionNote(String eventSubscriptionNote) {
		this.eventSubscriptionNote = eventSubscriptionNote;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getFaxNumber() {
		return faxNumber;
	}
	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getRegistrationStatus() {
		return registrationStatus;
	}
	public void setRegistrationStatus(String registrationStatus) {
		this.registrationStatus = registrationStatus;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getSalesPerson() {
		return salesPerson;
	}
	public void setSalesPerson(String salesPerson) {
		this.salesPerson = salesPerson;
	}
	public String getServiceCenter() {
		return serviceCenter;
	}
	public void setServiceCenter(String serviceCenter) {
		this.serviceCenter = serviceCenter;
	}
	public int getSalesPrice() {
		return salesPrice;
	}
	public void setSalesPrice(int salesPrice) {
		this.salesPrice = salesPrice;
	}
	public int getAmountPaid() {
		return amountPaid;
	}
	public void setAmountPaid(int amountPaid) {
		this.amountPaid = amountPaid;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getRegisteredDate() {
		return registeredDate;
	}
	public void setRegisteredDate(String registeredDate) {
		this.registeredDate = registeredDate;
	}
	public int getUserEdited() {
		return userEdited;
	}
	public void setUserEdited(int userEdited) {
		this.userEdited = userEdited;
	}
	public String getPoNumber() {
		return poNumber;
	}
	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}
	public String getMailSentStatus() {
		return mailSentStatus;
	}
	public void setMailSentStatus(String mailSentStatus) {
		this.mailSentStatus = mailSentStatus;
	}
	public String[] getEventLocationFilter() {
		return eventLocationFilter;
	}
	public void setEventLocationFilter(String[] eventLocationFilter) {
		this.eventLocationFilter = eventLocationFilter;
	}
	public String[] getEventCategoryFilter() {
		return eventCategoryFilter;
	}
	public void setEventCategoryFilter(String[] eventCategoryFilter) {
		this.eventCategoryFilter = eventCategoryFilter;
	}
	public String getFieldValue() {
		return fieldValue;
	}
	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public EventModel getEventsCustomFieldList() {
		return eventsCustomFieldList;
	}
	public void setEventsCustomFieldList(EventModel eventsCustomFieldList) {
		this.eventsCustomFieldList = eventsCustomFieldList;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public String getAdditionalInformation() {
		return additionalInformation;
	}
	public void setAdditionalInformation(String additionalInformation) {
		this.additionalInformation = additionalInformation;
	}

}
