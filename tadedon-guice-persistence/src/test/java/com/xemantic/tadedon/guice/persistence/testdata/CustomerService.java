/*
 * Copyright 2010 Xemantic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xemantic.tadedon.guice.persistence.testdata;

import com.google.inject.ImplementedBy;
import com.xemantic.tadedon.guice.persistence.Transactional;


/**
 * Test service.
 * <p>
 * Created on Mar 25, 2010
 *
 * @author hshsce
 */
@ImplementedBy(DefaultCustomerService.class)
public interface CustomerService {

	/**
	 * Capitalizes customer name.
	 *
	 * @param customer the customer.
	 * @return processed customer.
	 */
	Customer capitalizeCustomerName(Customer customer);

	/**
	 * Returns customer with given id.
	 *
	 * @param id the customer id
	 * @return the customer instance.
	 */
	@Transactional(readOnly=true)
	Customer getCustomerById(String id);

	/**
	 * Persists the {@code customer}.
	 *
	 * @param customer the customer instance.
	 */
	@Transactional
	void persistCustomer(Customer customer);

}
