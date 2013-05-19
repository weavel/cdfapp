package cafe.feestneus;

import java.util.Vector;

public class ObjectCalendar
{
	private Vector<ObjectEvent> events = new Vector<ObjectEvent>();
	private Vector<ObjectVisit> visits = new Vector<ObjectVisit>();
	
	public boolean contains(ObjectVisit testVisit)
	{
		boolean found = false;
		for(int i = 0; i < visits.size(); i++)
		{
			if(!visits.get(i).removed)
			{
				if(visits.get(i).isIdentical(testVisit))
				{
					found = true;
				}
			}
		}
		return found;
	}

	public void add(ObjectVisit visit)
	{
		boolean identicalFound = false;
		for(int i = visits.size() - 1; i >= 0; i--)
		{
			if(visits.get(i).isIdentical(visit))
			{
				if(visits.get(i).removed)
				{
					visits.get(i).removed = false;
					visits.get(i).edited = true;
				}
			}
		}
		if(!identicalFound)
		{
			visits.add(visit);
		}
	}

	public void remove(ObjectVisit removeVisit)
	{
		for(int i = visits.size() - 1; i >= 0; i--)
		{
			if(visits.get(i).isIdentical(removeVisit))
			{
				visits.get(i).removed = true;
				visits.get(i).edited = true;
			}
		}
	}

	public Vector<ObjectVisit> getVisits()
	{
		return visits;
	}

	public void setVisits(Vector<ObjectVisit> newVisitSet)
	{
		if(newVisitSet != null)
		{
			visits = newVisitSet;
		}
	}

	//Events
	public boolean contains(ObjectEvent testEvent)
	{
		boolean found = false;
		for(int i = 0; i < events.size(); i++)
		{
			if(events.get(i).isIdentical(testEvent))
			{
				found = true;
			}
		}
		return found;
	}

	public void add(ObjectEvent event)
	{
		events.add(event);
	}

	public void remove(ObjectEvent removeEvent)
	{
		for(int i = events.size() - 1; i >= 0; i--)
		{
			if(events.get(i).isIdentical(removeEvent))
			{
				events.remove(i);
			}
		}
	}

	public String getEventType(ObjectEvent event)
	{
		for(int i = 0; i < events.size(); i++)
		{
			if(events.get(i).isIdentical(event))
			{
				return events.get(i).eventType;
			}
		}
		return "";
	}

	public boolean getAboValid(ObjectEvent event)
	{
		for(int i = 0; i< events.size(); i++)
		{
			if(events.get(i).isIdentical(event) && (!events.get(i).aboValid))
			{
				return false;
			}
		}
		return true;
	}

	public void setEvents(Vector<ObjectEvent> newEventSet)
	{
		if(newEventSet != null)
		{
			events = newEventSet;
		}
	}

	public Vector<ObjectEvent> getEvents()
	{
		return events;
	}
}
