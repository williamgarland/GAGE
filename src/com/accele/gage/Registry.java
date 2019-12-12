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
public class Registry<T extends Indexable> implements Cleanable {

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
	 * Registers the specified {@code entry} to this {@code Registry}.
	 * <p>
	 * An {@link java.lang.IllegalArgumentException IllegalArgumentException}
	 * will be thrown if the registry already contains an entry with the same registry ID as the argument.
	 * All {@link com.accele.gage.callbacks.RegistryCallback RegistryCallback}{@code s} in the entry-add callback list will be invoked if the entry can be registered.
	 * </p>
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
	 * Registers the specified {@code entries} to this {@code Registry}.
	 * <p>
	 * An {@link java.lang.IllegalArgumentException IllegalArgumentException}
	 * will be thrown if the registry already contains an entry with the same registry ID as the argument.
	 * All {@link com.accele.gage.callbacks.RegistryCallback RegistryCallback}{@code s} in the entry-add callback list will be invoked for each entry that can be registered.
	 * </p>
	 * @param entries the entries to add
	 * @throws IllegalArgumentException if the registry already contains an entry with the same registry ID as the argument
	 */
	@SafeVarargs
	public final void registerAll(T... entries) {
		StringBuilder sb = new StringBuilder();
		boolean error = false;
		for (T entry : entries) {
			try {
				register(entry);
			} catch (IllegalArgumentException e) {
				error = true;
				sb.append("\n\t" + entry.getRegistryId());
			}
		}
		if (error)
			throw new IllegalArgumentException("Duplicate entries for the following ids:" + sb.toString());
	}
	
	/**
	 * Registers the specified {@code entries} to this {@code Registry}.
	 * <p>
	 * An {@link java.lang.IllegalArgumentException IllegalArgumentException}
	 * will be thrown if the registry already contains an entry with the same registry ID as the argument.
	 * All {@link com.accele.gage.callbacks.RegistryCallback RegistryCallback}{@code s} in the entry-add callback list will be invoked for each entry that can be registered.
	 * </p>
	 * @param entries the entries to add
	 * @throws IllegalArgumentException if the registry already contains an entry with the same registry ID as the argument
	 */
	public void registerAll(Collection<T> entries) {
		for (T entry : entries)
			register(entry);
	}
	
	/**
	 * Returns the entry with the specified {@code id}.
	 * <p>
	 * An {@link java.lang.IllegalArgumentException IllegalArgumentException} will be thrown if the registry does not contain an entry with the specified registry ID.
	 * </p>
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
	 * <p>
	 * An {@link java.lang.IllegalArgumentException IllegalArgumentException} will be thrown if the registry does not contain an entry with the specified registry ID.
	 * All {@link com.accele.gage.callbacks.RegistryCallback RegistryCallbacks} in the entry-remove callback list will be invoked if the entry exists.
	 * </p>
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
	 * Removes all entries from the {@code Registry}.
	 * <p>
	 * All {@link com.accele.gage.callbacks.RegistryCallback RegistryCallbacks} in the entry-remove callback list will be invoked for each entry in the {@code Registry}.
	 * </p>
	 */
	public void clear() {
		for (T entry : entries.values())
			entryRemoveCallbacks.forEach(c -> c.call(entry));
		entries.clear();
	}
	
	/**
	 * Returns an immutable view of all entries in the {@code Registry}.
	 * 
	 * @return an unmodifiable {@link java.util.Collection Collection} of all entries in the {@code Registry}
	 */
	public Collection<T> getEntries() {
		return Collections.unmodifiableCollection(entries.values());
	}
	
	@Override
	public void clean() {
		entries.values().forEach(e -> { if (e instanceof Cleanable) ((Cleanable) e).clean(); });
	}
	
}
