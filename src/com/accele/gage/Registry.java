package com.accele.gage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.accele.gage.callbacks.RegistryCallback;

/**
 * A general-purpose storage class for engine resources.
 * 
 * <p>
 * All objects used in GAGE must be registered in an instance of this class. The main
 * {@link com.accele.gage.GAGE GAGE} class contains the registries for all engine resources.
 * </p>
 * 
 * @param <T> the type of {@code Indexable} object this registry will store
 * 
 * @author William Garland
 * @version 1.0.0
 * @since 1.0.0
 * @see com.accele.gage.GAGE GAGE
 * @see com.accele.gage.Indexable Indexable
 */
public class Registry<T extends Indexable> {

	private Map<String, T> entries;
	private List<RegistryCallback<T>> entryAddCallbacks;
	private List<RegistryCallback<T>> entryRemoveCallbacks;
	
	Registry() {
		this.entries = new HashMap<>();
		this.entryAddCallbacks = new ArrayList<>();
		this.entryRemoveCallbacks = new ArrayList<>();
	}
	
	/**
	 * Adds an entry-add callback to this {@code Registry}. The callback will be invoked whenever an entry is added to the registry.
	 * 
	 * @param callback the {@link com.accele.gage.callbacks.RegistryCallback RegistryCallback} to add
	 */
	public void addEntryAddCallback(RegistryCallback<T> callback) {
		entryAddCallbacks.add(callback);
	}
	
	/**
	 * Adds an entry-remove callback to this {@code Registry}. The callback will be invoked whenever an entry is removed from the registry.
	 * 
	 * @param callback the {@link com.accele.gage.callbacks.RegistryCallback RegistryCallback} to add
	 */
	public void addEntryRemoveCallback(RegistryCallback<T> callback) {
		entryRemoveCallbacks.add(callback);
	}
	
	/**
	 * Registers the specified {@code entry} to this {@code Registry}. An {@link java.lang.IllegalArgumentException IllegalArgumentException}
	 * will be thrown if the registry already contains an entry with the same registry ID as the argument.
	 * All {@link com.accele.gage.callbacks.RegistryCallback RegistryCallback}{@code s} in the entry-add callback list will be invoked if the entry can be registered.
	 * 
	 * @param entry the entry to add
	 * @throws IllegalArgumentException if the registry already contains an entry with the same registry ID as the argument
	 */
	public void register(T entry) {
		if (entries.containsKey(entry.getRegistryId()))
			throw new IllegalArgumentException("Duplicate entry for id \"" + entry.getRegistryId() + "\".");
		entries.put(entry.getRegistryId(), entry);
		entryAddCallbacks.forEach(c -> c.call(entry));
	}
	
	/**
	 * Returns the entry with the specified {@code id}.
	 * An {@link java.lang.IllegalArgumentException IllegalArgumentException} will be thrown if the registry does not contain an entry with the specified registry ID.
	 * 
	 * @param id the registry ID of the target entry
	 * @return the entry with the specified {@code id}
	 * @throws IllegalArgumentException if the registry does not contain an entry with the specified registry ID
	 */
	public T getEntry(String id) {
		return entries.values().stream().filter(p -> p.getRegistryId().equals(id)).findAny().orElseThrow(() -> new IllegalArgumentException("Invalid entry for id \"" + id + "\"."));
	}
	
	/**
	 * Removes the entry with the specified {@code id} and returns the entry.
	 * An {@link java.lang.IllegalArgumentException IllegalArgumentException} will be thrown if the registry does not contain an entry with the specified registry ID.
	 * All {@link com.accele.gage.callbacks.RegistryCallback RegistryCallbacks} in the entry-remove callback list will be invoked if the entry exists.
	 * 
	 * @param id the registry ID of the target entry
	 * @return the entry with the specified {@code id}
	 * @throws IllegalArgumentException if the registry does not contain an entry with the specified registry ID
	 */
	public T removeEntry(String id) {
		if (!entries.containsKey(id))
			throw new IllegalArgumentException("Invalid entry for id \"" + id + "\".");
		T entry = entries.remove(id);
		entryRemoveCallbacks.forEach(c -> c.call(entry));
		return entry;
	}
	
	/**
	 * Returns an immutable view of all entries in the {@code Registry}.
	 * 
	 * @return an unmodifiable {@link java.util.Collection Collection} of all entries in the {@code Registry}
	 */
	public Collection<T> getEntries() {
		return Collections.unmodifiableCollection(entries.values());
	}
	
}
